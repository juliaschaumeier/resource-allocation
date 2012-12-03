package allocation;

import java.util.HashSet;
import java.util.Set;

import com.google.inject.AbstractModule;

import uk.ac.imperial.presage2.core.simulator.InjectedSimulation;
import uk.ac.imperial.presage2.core.simulator.Scenario;
import uk.ac.imperial.presage2.rules.RuleModule;
import uk.ac.imperial.presage2.rules.RuleStorage;
import uk.ac.imperial.presage2.util.environment.AbstractEnvironmentModule;
import uk.ac.imperial.presage2.util.network.NetworkModule;

public class Simulation extends InjectedSimulation {

	@Override
	protected Set<AbstractModule> getModules() {
		Set<AbstractModule> modules = new HashSet<AbstractModule>();

		modules.add(NetworkModule.noNetworkModule());
		modules.add(new AbstractEnvironmentModule()
				.setStorage(RuleStorage.class));
		modules.add(new RuleModule());

		return modules;
	}

	@Override
	protected void addToScenario(Scenario s) {

	}

}
