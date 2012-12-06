package allocation.facts;

import allocation.Phase;

public class CommonPool {

	public enum RefillRate {
		LOW, MEDIUM, HIGH
	};

	final int id;
	double resourceLevel;
	final double maxLevel;
	boolean canRefill = false;
	int lastFilled = 0;
	final Institution institution;
	Phase state = Phase.CFV;
	int round = 0;

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
	}

	public double getMaxLevel() {
		return maxLevel;
	}

	public boolean isCanRefill() {
		return canRefill;
	}

	public void setCanRefill(boolean canRefill) {
		this.canRefill = canRefill;
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

	@Override
	public String toString() {
		return "CommonPool " + id + " [resourceLevel=" + resourceLevel
				+ ", maxLevel=" + maxLevel + ", state=" + state + " , round="
				+ round + "]";
	}

}
