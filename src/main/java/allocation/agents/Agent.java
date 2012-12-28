package allocation.agents;

import org.drools.factmodel.traits.Traitable;

import uk.ac.imperial.presage2.core.util.random.Random;

@Traitable
public class Agent {

	String name;
	int pool;
	int institutionId;
	final double compliancyDegree;
	final double standardRequest;
	final double preferredRequest;
	boolean active = true;
	Role role;
	Behaviour behav;

	public Agent(String name, double compliancyDegree, double standardRequest,
			int pool, int iid, Role initialRole) {
		super();
		this.name = name;
		this.compliancyDegree = compliancyDegree;
		// oscillate with an amplitude of 0.1
		this.standardRequest = standardRequest;
		this.preferredRequest = standardRequest * compliancyDegree
				* (1 + (0.2 * Random.randomDouble() - 0.1));
		this.pool = pool;
		this.institutionId = iid;
		this.role = initialRole;
		// role = (this.institutionId >= 0 ? Role.MEMBER : Role.NONMEMBER);
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

	public String getName() {
		return name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	/*@Override
	public void incrementTime() {
		super.incrementTime();
		poolMonitor = poolService.getPool(pool);
		institution = poolService.getInstitution(institutionId);
		behav = AgentBehaviour.getBehaviour(this, behav);
		behav.doBehaviour();
	}

	public Set<Allocation> doAllocation(CommonPool pool, List<Demand> demands) {
		if (behav instanceof HeadBehaviour) {
			return ((HeadBehaviour) behav).allocate(pool, demands);
		}
		logger.warn("Got told to allocate but I don't have a HeadBehaviour!");
		return Collections.emptySet();
	}

	void act(Action a) {
		try {
			environment.act(a, getID(), authkey);
		} catch (ActionHandlingException e) {
			logger.warn("Couldn't act", e);
		}
	}*/
	
	public double appropriate() {
		return 0;
	}

}
