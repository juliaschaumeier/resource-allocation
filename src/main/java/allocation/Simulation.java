package allocation;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.drools.runtime.StatefulKnowledgeSession;

import uk.ac.imperial.presage2.core.IntegerTime;
import uk.ac.imperial.presage2.core.Time;
import uk.ac.imperial.presage2.core.event.EventBus;
import uk.ac.imperial.presage2.core.event.EventListener;
import uk.ac.imperial.presage2.core.simulator.EndOfTimeCycle;
import uk.ac.imperial.presage2.core.simulator.InjectedSimulation;
import uk.ac.imperial.presage2.core.simulator.Parameter;
import uk.ac.imperial.presage2.core.simulator.Scenario;
import uk.ac.imperial.presage2.core.util.random.Random;
import uk.ac.imperial.presage2.rules.RuleModule;
import uk.ac.imperial.presage2.rules.RuleStorage;
import uk.ac.imperial.presage2.util.environment.AbstractEnvironmentModule;
import uk.ac.imperial.presage2.util.network.NetworkModule;
import allocation.actions.AgentActionHandler;
import allocation.agents.Agent;
import allocation.agents.Role;
import allocation.facts.CommonPool;
import allocation.facts.Institution;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;

public class Simulation extends InjectedSimulation {

	private final Logger logger = Logger.getLogger(Simulation.class);
	private StatefulKnowledgeSession session;
	private Time t = new IntegerTime();

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

	@Parameter(name = "principle2", optional = true)
	public boolean principle2 = true;
	@Parameter(name = "principle3", optional = true)
	public boolean principle3 = true;
	@Parameter(name = "principle4", optional = true)
	public boolean principle4 = true;
	@Parameter(name = "principle5", optional = true)
	public boolean principle5 = true;
	@Parameter(name = "principle6", optional = true)
	public boolean principle6 = true;

	Set<Institution> institutions = new HashSet<Institution>();

	public Simulation(Set<AbstractModule> modules) {
		super(modules);
	}

	@Inject
	public void setSession(StatefulKnowledgeSession session) {
		this.session = session;
	}

	@Inject
	public void eventBusSubscribe(EventBus eb) {
		eb.subscribe(this);
	}

	@Override
	protected Set<AbstractModule> getModules() {
		Set<AbstractModule> modules = new HashSet<AbstractModule>();

		modules.add(new AbstractEnvironmentModule()
				.addActionHandler(AgentActionHandler.class)
				.addParticipantGlobalEnvironmentService(PoolService.class)
				.setStorage(RuleStorage.class));
		modules.add(new RuleModule().addClasspathDrlFile("environment.drl")
				.addClasspathDrlFile("institution.drl"));
		modules.add(NetworkModule.noNetworkModule());

		return modules;
	}

	@Override
	protected void addToScenario(Scenario s) {

		session.setGlobal("logger", logger);
		double initialLevel = 2 * standardRequest * agents;
		CommonPool pool0 = new CommonPool(0, initialLevel, initialLevel);
		Institution i0 = new Institution(0, agents, principle2, principle3,
				principle4, principle5, principle6);
		i0.addPool(pool0);
		institutions.add(i0);
		session.insert(pool0);
		session.insert(i0);
		session.insert(t);

		for (int i = 0; i < agents; i++) {
			Agent a;
			if (i < numCheat) {
				a = new Agent("elf " + i, 1 + greedMax * Random.randomDouble(),
						standardRequest, 0, 0);
			} else {
				a = new Agent("elf " + i, 1 - altrMax * Random.randomDouble(),
						standardRequest, 0, 0);
			}
			s.addParticipant(a);
			session.insert(a);
			if (i == 0)
				a.setRole(Role.HEAD);
		}
	}

	@EventListener
	public void incrementTime(EndOfTimeCycle e) {
		t.increment();
		session.update(session.getFactHandle(t), t);
		for (Institution i : institutions) {
			Phase next = Phase.values()[(i.getState().ordinal() + 1)
					% Phase.values().length];
			i.setState(next);
			if (next == Phase.CFV)
				i.incrementRound();
			session.update(session.getFactHandle(i), i);
			logger.info(i);
		}
	}

}
