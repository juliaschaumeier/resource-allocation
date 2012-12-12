package allocation.agents;

import uk.ac.imperial.presage2.core.Action;
import uk.ac.imperial.presage2.core.environment.ActionHandlingException;
import uk.ac.imperial.presage2.core.environment.UnavailableServiceException;
import uk.ac.imperial.presage2.core.messaging.Input;
import uk.ac.imperial.presage2.core.util.random.Random;
import uk.ac.imperial.presage2.util.participant.AbstractParticipant;
import allocation.PoolService;
import allocation.actions.Allocation;
import allocation.facts.Institution;
import allocation.facts.ResourceMonitor;

public class Agent extends AbstractParticipant {

	int pool;
	int institutionId;
	final double compliancyDegree;
	final double standardRequest;
	final double preferredRequest;
	boolean active = true;
	Role role;
	Behaviour behav;

	PoolService poolService;
	ResourceMonitor poolMonitor;
	Institution institution;

	Allocation allocation = null;

	public Agent(String name, double compliancyDegree, double standardRequest,
			int pool, int iid) {
		super(Random.randomUUID(), name);
		this.compliancyDegree = compliancyDegree;
		// oscillate with an amplitude of 0.1
		this.standardRequest = standardRequest;
		this.preferredRequest = standardRequest * compliancyDegree
				* (1 + (0.2 * Random.randomDouble() - 0.1));
		this.pool = pool;
		this.institutionId = iid;
		role = (this.institutionId >= 0 ? Role.MEMBER : Role.NONMEMBER);
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

	public int getInstitutionId() {
		return institutionId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Allocation getAllocation() {
		return allocation;
	}

	public void setAllocation(Allocation allocation) {
		this.allocation = allocation;
	}

	@Override
	protected void processInput(Input in) {
	}

	@Override
	public void incrementTime() {
		super.incrementTime();
		poolMonitor = poolService.getPool(pool);
		institution = poolService.getInstitution(institutionId);
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
