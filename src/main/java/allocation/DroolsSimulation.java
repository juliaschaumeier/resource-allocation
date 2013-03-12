package allocation;

import java.util.HashSet;

import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;

import uk.ac.imperial.presage2.core.IntegerTime;
import uk.ac.imperial.presage2.core.Time;
import uk.ac.imperial.presage2.core.db.DatabaseModule;
import uk.ac.imperial.presage2.core.db.DatabaseService;
import uk.ac.imperial.presage2.core.db.StorageService;
import uk.ac.imperial.presage2.core.db.persistent.PersistentSimulation;
import uk.ac.imperial.presage2.core.simulator.RunnableSimulation;
import uk.ac.imperial.presage2.core.simulator.Scenario;
import uk.ac.imperial.presage2.core.util.random.Random;
import allocation.facts.CommonPool;
import allocation.facts.Institution;
import allocation.facts.RaMethod;
import allocation.facts.Profile;
import allocation.newagents.Head;
import allocation.newagents.Member;
import allocation.newagents.NonMember;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;

public class DroolsSimulation {

	private final Logger logger = Logger.getLogger(DroolsSimulation.class);
	private final Simulation sim;
	private final boolean enableDroolsLogger;

	DatabaseService db = null;
	StorageService sto = null;
	PersistentSimulation simPersist = null;

	DroolsSimulation(Simulation sim, boolean enableDroolsLogger) {
		super();
		this.sim = sim;
		this.enableDroolsLogger = enableDroolsLogger;
	}

	public static void main(String[] args) throws Exception {
		System.out.print("Parameters given: ");
		if (args.length == 1) {
			System.out.print("None.");
		}
		for (int i = 0; i < args.length; i++) {
			System.out.print(args[i] + ",");
		}
		System.out.println();

		Simulation sim = (Simulation) RunnableSimulation.newFromClassName(
				Simulation.class.getCanonicalName(),
				new HashSet<AbstractModule>());
		sim.parseParameters(args);

		DroolsSimulation dSim = new DroolsSimulation(sim, true);
		dSim.run();

	}

	void run() {
		// drools initialisation
		String[] ruleSets = { "environment.drl", "institution.drl", "store.drl" };
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();
		// compile rule files
		kbuilder.add(
				ResourceFactory.newClassPathResource("simulation.rf",
						sim.getClass()), ResourceType.DRF);
		for (String path : ruleSets) {
			kbuilder.add(
					ResourceFactory.newClassPathResource(path, sim.getClass()),
					ResourceType.DRL);
		}
		if (kbuilder.hasErrors()) {
			logger.fatal(kbuilder.getErrors().toString());
			System.exit(1);
		}

		KnowledgeBaseConfiguration baseConf = KnowledgeBaseFactory
				.newKnowledgeBaseConfiguration();
		// baseConf.setOption(EventProcessingOption.STREAM);
		KnowledgeSessionConfiguration sessionConf = KnowledgeBaseFactory
				.newKnowledgeSessionConfiguration();
		// sessionConf.setOption(ClockTypeOption.get("pseudo"));

		// create KnowledgeBase from builder
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(baseConf);
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		// create session
		StatefulKnowledgeSession session = kbase.newStatefulKnowledgeSession(
				sessionConf, null);
		KnowledgeRuntimeLogger droolsLogger = null;
		if (enableDroolsLogger) {
			droolsLogger = KnowledgeRuntimeLoggerFactory.newFileLogger(session,
					"test");
		}

		/*
		 * session.addEventListener(new DefaultAgendaEventListener() { public
		 * void afterActivationFired(AfterActivationFiredEvent event) {
		 * super.afterActivationFired(event); logger.debug(event); } });
		 */

		/*
		 * // database load DatabaseService db = null; StorageService sto =
		 * null; PersistentSimulation simPersist = null; try { // load db and
		 * storage services with an injector // the use of scenario builder is a
		 * hack to get sql storage to work. Injector dbInjector = new
		 * Scenario.Builder(DatabaseModule.load()) .getInjector(); db =
		 * dbInjector.getInstance(DatabaseService.class); sto =
		 * dbInjector.getInstance(StorageService.class); db.start(); simPersist
		 * = sto.createSimulation("Resource allocation",
		 * DroolsSimulation.class.getName(), "INITIALISING", sim.finishTime);
		 * for (String s : sim.getParameters().keySet()) {
		 * simPersist.addParameter(s, sim.getParameter(s)); } } catch (Exception
		 * e) { logger.warn("Error loading database", e); }
		 */

		// session globals
		session.setGlobal("logger", logger);
		session.setGlobal("sto", sto);

		Time t = new IntegerTime();
		// create a single common pool
		double initialLevel = 2 * sim.standardRequest * sim.agents;
		CommonPool pool0 = new CommonPool(0, initialLevel, initialLevel,
				sim.outAppropriationFrequency, sim.outImproveFrequency, sim.refScheme);

		// create a single institution governing the pool.
		Institution i0 = new Institution(session, 0, sim.agents, pool0,
				sim.principle2, sim.principle3, sim.principle4, sim.principle5,
				sim.principle6, sim.unintentionalError, sim.voteHead, sim.voteRaMethod, sim.headDecides,
				sim.monitoringLevel, sim.monitoringCost, sim.outMonitoringLevel,
				sim.outMonitoringCost, sim.noisePercentage, sim.noiseLevel, 
				sim.appealtime, sim.samplingrateRaMethod, sim.samplingrateHead);

		// insert pool and institution into drools session.
		session.insert(pool0);
		session.insert(i0);
		session.insert(t);

		double comp;// for (initial)compiancyDegree
		double rd;
		Profile prof;
		RaMethod prAbun;
		RaMethod prCris;
		// create member agents
		for (int i = 0; i < sim.agents; i++) {
			Member a;			
			if (i < sim.numCheat) {// cheating member
				comp = 1 + sim.greedMax * Random.randomDouble();
			} else {// good member
				comp = 1 - sim.altrMax * Random.randomDouble();
			}
			//set justice Principles in Abundance and Crisis
			rd = Random.randomDouble();
			if (rd < sim.justicePrPercentage1){
				prAbun = RaMethod.EQUITY;
				if(Random.randomDouble() < sim.justicePrTransition1){
					prCris = RaMethod.EQUALITY;
				}
				else {
					prCris = RaMethod.EQUITY;
				}
			}else if (rd < sim.justicePrPercentage2){
				prAbun = RaMethod.EQUALITY;
				if(Random.randomDouble() < sim.justicePrTransition2){
					prCris = RaMethod.NEED;
				}
				else {
					prCris = RaMethod.EQUALITY;
				}
			}else{
				prAbun = RaMethod.NEED;
				prCris = RaMethod.NEED;
			}
			//set agent Profiles
			if (Random.randomDouble() < sim.profilePercentage){
				prof = Profile.MERITIOUS;
			} else {
				prof = Profile.NEEDY;
			}
			// set the first agent to be the head initially.
			if (i==0){
				a = new Head("elf " + i, i, comp, comp, sim.standardRequest,
						sim.noRequestPercentage, sim.changeBehaviourPercentage,
						sim.improveBehaviour, 0, 0, prof, prAbun, prCris, sim.judgeSize, sim.judgeTolerance,
						sim.useSat, sim.initialSat, sim.leaveSat, sim.increaseSat, sim.decreaseSat);
			}
			else{
				a = new Member("elf " + i, i, comp, comp, sim.standardRequest,
						sim.noRequestPercentage, sim.changeBehaviourPercentage,
						sim.improveBehaviour, 0, 0, prof, prAbun, prCris, sim.judgeSize, sim.judgeTolerance,
						sim.useSat, sim.initialSat, sim.leaveSat, sim.increaseSat, sim.decreaseSat);
			}
			
			
			
			session.insert(a);
		}
		// create non member agents
		for (int i = 0; i < sim.outAgents; i++) {
			NonMember a;
			if (i < sim.outNumCheat) {
				comp = 1 + sim.greedMax * Random.randomDouble();
			} else {
				comp = 1 - sim.altrMax * Random.randomDouble();
			}
			//set justice Principles in Abundance and Crisis
			rd = Random.randomDouble();
			if (rd < sim.justicePrPercentage1){
				prAbun = RaMethod.EQUITY;
				if(Random.randomDouble() < sim.justicePrTransition1){
					prCris = RaMethod.EQUALITY;
				}
				else {
					prCris = RaMethod.EQUITY;
				}
			}else if (rd < sim.justicePrPercentage2){
				prAbun = RaMethod.EQUALITY;
				if(Random.randomDouble() < sim.justicePrTransition2){
					prCris = RaMethod.NEED;
				}
				else {
					prCris = RaMethod.EQUALITY;
				}
			}else{
				prAbun = RaMethod.NEED;
				prCris = RaMethod.NEED;
			}
			//set agent Profiles
			if (Random.randomDouble() < sim.profilePercentage){
				prof = Profile.MERITIOUS;
			} else {
				prof = Profile.NEEDY;
			}
			
			a = new NonMember("outelf " + i, i+sim.agents, comp, comp,
					sim.standardRequest, 0, prof, prAbun, prCris, sim.judgeSize, sim.judgeTolerance);
			session.insert(a);
		}

		// simulation loop
		if (simPersist != null) {
			simPersist.setState("RUNNING");
			simPersist.setStartedAt(System.currentTimeMillis());
		}
		try {
			while (t.intValue() < sim.finishTime) {
				logger.info("Round " + t.intValue());
				session.startProcess("allocation.Simulation");
				session.fireAllRules();
				t.increment();
				session.update(session.getFactHandle(t), t);
				if (simPersist != null)
					simPersist.setCurrentTime(t.intValue());
			}
		} finally {
			if (enableDroolsLogger)
				droolsLogger.close();
		}
		if (simPersist != null) {
			simPersist.setState("COMPLETE");
			simPersist.setFinishedAt(System.currentTimeMillis());
		}
		logger.info("Finished!");
		if (db != null)
			db.stop();
	}
}
