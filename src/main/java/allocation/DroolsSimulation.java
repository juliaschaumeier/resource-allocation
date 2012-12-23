package allocation;

import java.util.HashSet;

import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.event.rule.AfterActivationFiredEvent;
import org.drools.event.rule.DefaultAgendaEventListener;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;

import uk.ac.imperial.presage2.core.IntegerTime;
import uk.ac.imperial.presage2.core.Time;
import uk.ac.imperial.presage2.core.simulator.RunnableSimulation;
import uk.ac.imperial.presage2.core.util.random.Random;
import allocation.agents.Agent;
import allocation.agents.Role;
import allocation.facts.CommonPool;
import allocation.facts.Institution;
import allocation.newagents.Head;
import allocation.newagents.Member;
import allocation.newagents.NonMember;

import com.google.inject.AbstractModule;

public class DroolsSimulation {

	private static final Logger logger = Logger
			.getLogger(DroolsSimulation.class);

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

		// drools initialisation
		String[] ruleSets = { "environment.drl", "agents.drl", "institution.drl" };
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
		KnowledgeRuntimeLogger droolsLogger = KnowledgeRuntimeLoggerFactory
				.newFileLogger(session, "test");

		session.addEventListener(new DefaultAgendaEventListener() {
			public void afterActivationFired(AfterActivationFiredEvent event) {
				super.afterActivationFired(event);
				logger.debug(event);
			}
		});

		// session globals
		session.setGlobal("logger", logger);

		Time t = new IntegerTime();
		// create a single common pool
		double initialLevel = 2 * sim.standardRequest * sim.agents;
		CommonPool pool0 = new CommonPool(0, initialLevel, initialLevel);

		// create a single institution governing the pool.
		Institution i0 = new Institution(session, 0, sim.agents,
				sim.principle2, sim.principle3, sim.principle4, sim.principle5,
				sim.principle6);
		i0.addPool(pool0);

		// insert pool and institution into drools session.
		session.insert(pool0);
		session.insert(i0);
		session.insert(t);

		// create member agents
		for (int i = 0; i < sim.agents; i++) {
			Member a;
			// set the first agent to be the head initially.
			if (i == 0) {
				a = new Head("elf " + i, 1 + sim.greedMax
						* Random.randomDouble(), sim.standardRequest, 0, 0);
			} else if (i < sim.numCheat) {
				// cheating member
				a = new Member("elf " + i, 1 + sim.greedMax
						* Random.randomDouble(), sim.standardRequest, 0, 0);
			} else {
				// good member
				a = new Member("elf " + i, 1 - sim.altrMax
						* Random.randomDouble(), sim.standardRequest, 0, 0);
			}
			session.insert(a);
		}
		// create non member agents
		for (int i = 0; i < sim.outAgents; i++) {
			NonMember a;
			if (i < sim.outNumCheat) {
				a = new NonMember("outelf " + i, 1 + sim.greedMax
						* Random.randomDouble(), sim.standardRequest, 0, 0.1);
			} else {
				a = new NonMember("outelf " + i, 1 + sim.altrMax
						* Random.randomDouble(), sim.standardRequest, 0, 0.1);
			}
			session.insert(a);
		}

		try {
			while (t.intValue() < sim.finishTime) {
				logger.info("Round " + t.intValue());
				session.startProcess("allocation.Simulation");
				session.fireAllRules();
				t.increment();
				session.update(session.getFactHandle(t), t);
			}
		} finally {
			droolsLogger.close();
		}
	}
}
