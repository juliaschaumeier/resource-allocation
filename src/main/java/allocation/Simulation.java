//http://www.horstmann.com/ccc/c_to_java.pdf // just for me... :-)
package allocation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

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

	// julia
	@Parameter(name = "outAgents", optional = true)
	public int outAgents = 20;
	@Parameter(name = "outNumCheat", optional = true)
	public int outNumCheat = 10;

	@Parameter(name = "standardRequest", optional = true)
	public double standardRequest = 50;

	@Parameter(name = "greedMax", optional = true)
	public double greedMax = 0.2;
	@Parameter(name = "altrMax", optional = true)
	public double altrMax = 0.0;

	// julia
	@Parameter(name = "monitoringLevel", optional = true)
	public double monitoringLevel = 0.1;
	@Parameter(name = "monitoringCost", optional = true)
	public double monitoringCost = 0.1 * standardRequest;
	@Parameter(name = "outMonitoringLevel", optional = true)
	public double outMonitoringLevel = 0.1;
	@Parameter(name = "outMonitoringCost", optional = true)
	public double outMonitoringCost = 0.1 * standardRequest;
	@Parameter(name = "outAppropriationFrequency", optional = true)
	public double outAppropriationFrequency = 0.1;
	@Parameter(name = "outImproveFrequency", optional = true)
	public double outImproveFrequency = 0.1;

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

		/*modules.add(new AbstractEnvironmentModule()
				.addActionHandler(AgentActionHandler.class)
				.addParticipantGlobalEnvironmentService(PoolService.class)
				.setStorage(RuleStorage.class));
		modules.add(new RuleModule().addClasspathDrlFile("environment.drl")
				.addClasspathDrlFile("institution.drl"));
		modules.add(NetworkModule.noNetworkModule());*/

		return modules;
	}

	@Override
	protected void addToScenario(Scenario s) {

		session.setGlobal("logger", logger);

		// create a single common pool
		double initialLevel = 2 * standardRequest * agents;
		CommonPool pool0 = new CommonPool(0, initialLevel, initialLevel);

		// create a single institution governing the pool.
		Institution i0 = new Institution(session, 0, agents, principle2,
				principle3, principle4, principle5, principle6);
		i0.addPool(pool0);
		institutions.add(i0);

		// insert pool and institution into drools session.
		session.insert(pool0);
		session.insert(i0);
		session.insert(t);

		// create member agents
		for (int i = 0; i < agents; i++) {
			Agent a;
			Role role = Role.MEMBER;
			// set the first agent to be the head initially.
			if (i == 0)
				role = Role.HEAD;

			if (i < numCheat) {
				// cheating member
				a = new Agent("elf " + i, 1 + greedMax * Random.randomDouble(),
						standardRequest, 0, 0, role);
			} else {
				// good member
				a = new Agent("elf " + i, 1 - altrMax * Random.randomDouble(),
						standardRequest, 0, 0, role);
			}
			//s.addParticipant(a);
			session.insert(a);

		}

		// create agents outside the institution
		for (int i = 0; i < outAgents; i++) {// julia
			Agent a;
			Role role = Role.NONMEMBER;
			if (i < outNumCheat) {
				// cheating agent
				a = new Agent("outelf " + i, 1 + greedMax
						* Random.randomDouble(), standardRequest, 0, 0, role);
			} else {
				// good agent
				a = new Agent("outelf " + i, 1 - altrMax
						* Random.randomDouble(), standardRequest, 0, 0, role);
			}
			//s.addParticipant(a);
			session.insert(a);

		}

	}

	void parseParameters(String[] args) throws Exception {
		// check for parameters in args
		Map<String, String> providedParams = new HashMap<String, String>();
		for (int i = 0; i < args.length; i++) {
			if (Pattern.matches("([a-zA-Z0-9_]+)=([a-zA-Z0-9_.,])+$", args[i])) {
				String[] pieces = args[i].split("=", 2);
				providedParams.put(pieces[0], pieces[1]);
			}
		}
		setParameters(providedParams);
	}

	/**
	 * Update the round phase for each institution in the simulation.
	 * 
	 * @param e
	 */
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
