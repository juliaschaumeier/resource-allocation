package allocation.facts;

import allocation.Phase;

public class CommonPool {

	public enum RefillRate {
		LOW, MEDIUM, HIGH
	};

	final int id;
	double resourceLevel;
	final double maxLevel;
	int lastFilled = 0;
	final Institution institution;
	Phase state = Phase.CFV;
	int round = 0;
	boolean depleated = false;

	boolean voteHead = false;
	boolean voteRaMethod = false;

	public CommonPool(int id, double initialLevel, double maxLavel,
			Institution i) {
		super();
		this.id = id;
		this.resourceLevel = initialLevel;
		this.maxLevel = maxLavel;
		this.institution = i;
	}

	public int getId() {
		return id;
	}

	public double getResourceLevel() {
		return resourceLevel;
	}

	public void setResourceLevel(double resourceLevel) {
		this.resourceLevel = resourceLevel;
		if (this.resourceLevel < 0) {
			this.depleated = true;
		}
	}

	public double getMaxLevel() {
		return maxLevel;
	}

	public int getLastFilled() {
		return lastFilled;
	}

	public void setLastFilled(int lastFilled) {
		this.lastFilled = lastFilled;
	}

	public Institution getInstitution() {
		return institution;
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

	public boolean isDepleated() {
		return depleated;
	}

	public boolean isVoteHead() {
		return voteHead;
	}

	public void setVoteHead(boolean voteHead) {
		this.voteHead = voteHead;
	}

	public boolean isVoteRaMethod() {
		return voteRaMethod;
	}

	public void setVoteRaMethod(boolean voteRaMethod) {
		this.voteRaMethod = voteRaMethod;
	}

	@Override
	public String toString() {
		return "CommonPool " + id + " [resourceLevel=" + resourceLevel
				+ ", maxLevel=" + maxLevel + ", state=" + state + " , round="
				+ round + "]";
	}

}
