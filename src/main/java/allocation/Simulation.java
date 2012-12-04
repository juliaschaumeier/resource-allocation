package allocation;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.drools.runtime.StatefulKnowledgeSession;

import uk.ac.imperial.presage2.core.IntegerTime;
import uk.ac.imperial.presage2.core.Time;
import uk.ac.imperial.presage2.core.TimeDriven;
import uk.ac.imperial.presage2.core.simulator.InjectedSimulation;
import uk.ac.imperial.presage2.core.simulator.Parameter;
import uk.ac.imperial.presage2.core.simulator.Scenario;
import uk.ac.imperial.presage2.core.util.random.Random;
import uk.ac.imperial.presage2.rules.RuleModule;
import uk.ac.imperial.presage2.rules.RuleStorage;
import uk.ac.imperial.presage2.util.environment.AbstractEnvironmentModule;
import uk.ac.imperial.presage2.util.network.NetworkModule;
import allocation.actions.AgentActionHandler;
import allocation.facts.CommonPool;
import allocation.facts.CommonPool.RefillRate;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;

public class Simulation extends InjectedSimulation implements TimeDriven {

	private final Logger logger = Logger.getLogger(Simulation.class);
	private StatefulKnowledgeSession session;
	private Time t = new IntegerTime();

	@Parameter(name = "refillRate", optional = true)
	public String refillRate = "medium";

	@Parameter(name = "agents", optional = true)
	public int agents = 100;

	@Parameter(name = "numCheat", optional = true)
	public int numCheat = 50;

	@Parameter(name = "standardRequest", optional = true)
	public double standardRequest = 50;

	@Parameter(name = "greedMax", optional = true)
	public double greedMax = 0.2;
	@Parameter(name = "altrMax", optional = true)
	public double altrMax = 0.0;

	public Simulation(Set<AbstractModule> modules) {
		super(modules);
	}

	@Inject
	public void setSession(StatefulKnowledgeSession session) {
		this.session = session;
	}

	@Override
	protected Set<AbstractModule> getModules() {
		Set<AbstractModule> modules = new HashSet<AbstractModule>();

		modules.add(new AbstractEnvironmentModule().addActionHandler(
				AgentActionHandler.class).setStorage(RuleStorage.class));
		modules.add(new RuleModule().addClasspathDrlFile("environment.drl"));
		modules.add(NetworkModule.noNetworkModule());

		return modules;
	}

	@Override
	protected void addToScenario(Scenario s) {
		s.addTimeDriven(this);
		session.setGlobal("logger", logger);
		RefillRate rate;
		try {
			rate = RefillRate.valueOf(refillRate);
		} catch (IllegalArgumentException e) {
			rate = RefillRate.MEDIUM;
		}
		double initialLevel = 2 * standardRequest * agents;
		session.insert(new CommonPool(initialLevel, initialLevel, rate));
		session.insert(t);

		for (int i = 0; i < agents; i++) {
			Agent a;
			if (i < numCheat) {
				a = new Agent("elf " + i, 1 + greedMax * Random.randomDouble(),
						standardRequest);
			} else {
				a = new Agent("elf " + i, 1 - altrMax * Random.randomDouble(),
						standardRequest);
			}
			s.addParticipant(a);
			session.insert(a);
		}
	}

	@Override
	public void incrementTime() {
		t.increment();
		session.update(session.getFactHandle(t), t);
	}

}
