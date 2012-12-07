package allocation.agents;

import uk.ac.imperial.presage2.core.Action;
import uk.ac.imperial.presage2.core.environment.ActionHandlingException;
import uk.ac.imperial.presage2.core.environment.UnavailableServiceException;
import uk.ac.imperial.presage2.core.messaging.Input;
import uk.ac.imperial.presage2.core.util.random.Random;
import uk.ac.imperial.presage2.util.participant.AbstractParticipant;
import allocation.PoolService;

public class Agent extends AbstractParticipant {

	int pool;
	final double compliancyDegree;
	final double preferredRequest;
	boolean active = true;
	Role role;
	Behaviour behav;

	PoolService poolService;

	public Agent(String name, double compliancyDegree, double standardRequest,
			int pool) {
		super(Random.randomUUID(), name);
		this.compliancyDegree = compliancyDegree;
		// oscillate with an amplitude of 0.1
		this.preferredRequest = standardRequest * compliancyDegree
				* (1 + (0.2 * Random.randomDouble() - 0.1));
		this.pool = pool;
		role = (this.pool >= 0 ? Role.member : Role.nonMember);
	}

	@Override
	public void initialise() {
		super.initialise();
		try {
			poolService = getEnvironmentService(PoolService.class);
		} catch (UnavailableServiceException e) {
			logger.warn("No pool service");
		}
	}

	public int getPool() {
		return pool;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	protected void processInput(Input in) {
	}

	@Override
	public void incrementTime() {
		super.incrementTime();
		behav = AgentBehaviour.getBehaviour(this);
		behav.doBehaviour();
	}

	void act(Action a) {
		try {
			environment.act(a, getID(), authkey);
		} catch (ActionHandlingException e) {
			logger.warn("Couldn't act", e);
		}
	}

}
