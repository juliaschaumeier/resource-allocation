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
import uk.ac.imperial.presage2.core.db.DatabaseModule;
import uk.ac.imperial.presage2.core.db.DatabaseService;
import uk.ac.imperial.presage2.core.db.StorageService;
import uk.ac.imperial.presage2.core.event.EventBus;
import uk.ac.imperial.presage2.core.event.EventListener;
import uk.ac.imperial.presage2.core.simulator.EndOfTimeCycle;
import uk.ac.imperial.presage2.core.simulator.InjectedSimulation;
import uk.ac.imperial.presage2.core.simulator.Parameter;
import uk.ac.imperial.presage2.core.simulator.Scenario;
import allocation.facts.Institution;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;

public class Simulation extends InjectedSimulation {

	private final Logger logger = Logger.getLogger(Simulation.class);
	private Time t = new IntegerTime();

	@Parameter(name = "agents", optional = true)
	public int agents = 100;

	@Parameter(name = "numCheat", optional = true)
	public int numCheat = 50;

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

	// percentage of agents that doesn't place a request or appropriate (per
	// timeslice)
	@Parameter(name = "noRequestPercentage", optional = true)
	public double noRequestPercentage = 0.1;
	@Parameter(name = "changeBehaviourPercentage", optional = true)
	public double changeBehaviourPercentage = 0.3;
	@Parameter(name = "improveBehaviour", optional = true)
	public double improveBehaviour = 0.5; // how much behaviour improves wrt
											// compliancyDegree

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
	@Parameter(name = "appealtime", optional = true)
	public int appealtime = 30;

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

	@Parameter(name = "unintentionalError", optional = true)
	public boolean unintentionalError = false;
	@Parameter(name = "noisePercentage", optional = true)
	public double noisePercentage = 0.05; // how many suffer from noise
	@Parameter(name = "noiseLevel", optional = true)
	public double noiseLevel = 0.1; // noise impact

	Set<Institution> institutions = new HashSet<Institution>();

	public Simulation(Set<AbstractModule> modules) {
		super(modules);
	}

	@Override
	protected Set<AbstractModule> getModules() {
		Set<AbstractModule> modules = new HashSet<AbstractModule>();

		/*
		 * modules.add(new AbstractEnvironmentModule()
		 * .addActionHandler(AgentActionHandler.class)
		 * .addParticipantGlobalEnvironmentService(PoolService.class)
		 * .setStorage(RuleStorage.class)); modules.add(new
		 * RuleModule().addClasspathDrlFile("environment.drl")
		 * .addClasspathDrlFile("institution.drl"));
		 * modules.add(NetworkModule.noNetworkModule());
		 */

		return modules;
	}

	@Override
	protected void addToScenario(Scenario s) {

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

}
