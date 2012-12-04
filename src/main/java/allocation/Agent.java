package allocation;

import uk.ac.imperial.presage2.core.environment.ActionHandlingException;
import uk.ac.imperial.presage2.core.messaging.Input;
import uk.ac.imperial.presage2.core.util.random.Random;
import uk.ac.imperial.presage2.util.participant.AbstractParticipant;
import allocation.actions.Appropriate;

public class Agent extends AbstractParticipant {

	final private double compliancyDegree;
	final private double preferredRequest;

	public Agent(String name, double compliancyDegree, double standardRequest) {
		super(Random.randomUUID(), name);
		this.compliancyDegree = compliancyDegree;
		// oscillate with an amplitude of 0.1
		this.preferredRequest = standardRequest * compliancyDegree
				* (1 + (0.2 * Random.randomDouble() - 0.1));
	}

	@Override
	protected void processInput(Input in) {
	}

	@Override
	public void incrementTime() {
		super.incrementTime();
		try {
			double appropriateAmount = preferredRequest;
			environment.act(new Appropriate(appropriateAmount), getID(),
					authkey);
		} catch (ActionHandlingException e) {
			logger.warn("Couldn't appropriate", e);
		}
	}

}
