package allocation.facts;

import java.util.HashSet;
import java.util.Set;

import allocation.Phase;

public class Institution {

	final int id;
	Phase state = Phase.CFV;
	int round = 0;

	final boolean principle1 = true;
	boolean principle2 = false;
	boolean principle3 = false;
	boolean principle4 = false;
	boolean principle5 = false;
	boolean principle6 = false;

	final int initialAgents;
	boolean voteHead = false;
	boolean voteRaMethod = false;

	RaMethod allocationMethod = RaMethod.QUEUE;

	Set<CommonPool> pools = new HashSet<CommonPool>();

	public Institution(int id, int initialAgents, boolean principle2,
			boolean principle3, boolean principle4, boolean principle5,
			boolean principle6) {
		super();
		this.id = id;
		this.principle2 = principle2;
		this.principle3 = principle3;
		this.principle4 = principle4;
		this.principle5 = principle5;
		this.principle6 = principle6;
		this.initialAgents = initialAgents;
	}

	public void addPool(CommonPool p) {
		pools.add(p);
	}

	public int getId() {
		return id;
	}

	public CommonPool getPool(int id) {
		for (CommonPool p : pools) {
			if (p.getId() == id)
				return p;
		}
		return null;
	}

	public Set<CommonPool> getPools() {
		return pools;
	}

	public boolean isPrinciple1() {
		return principle1;
	}

	public boolean isPrinciple2() {
		return principle2;
	}

	public boolean isPrinciple3() {
		return principle3;
	}

	public boolean isPrinciple4() {
		return principle4;
	}

	public boolean isPrinciple5() {
		return principle5;
	}

	public boolean isPrinciple6() {
		return principle6;
	}

	public Phase getState() {
		return state;
	}

	public void setState(Phase state) {
		this.state = state;
	}

	public int getRound() {
		return round;
	}

	public void incrementRound() {
		++round;
	}

	public int getInitialAgents() {
		return initialAgents;
	}

	public boolean isVoteHead() {
		return voteHead;
	}

	public boolean isVoteRaMethod() {
		return voteRaMethod;
	}

	public void setVoteHead(boolean voteHead) {
		this.voteHead = voteHead;
	}

	public void setVoteRaMethod(boolean voteRaMethod) {
		this.voteRaMethod = voteRaMethod;
	}

	public RaMethod getAllocationMethod() {
		return allocationMethod;
	}

	public void setAllocationMethod(RaMethod allocationMethod) {
		this.allocationMethod = allocationMethod;
	}

	@Override
	public String toString() {
		return "Institution [id=" + id + ", state=" + state + ", round="
				+ round + ", allocationMethod=" + allocationMethod + "]";
	}

}
