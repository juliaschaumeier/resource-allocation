package allocation.facts;

public class CommonPool {

	public enum RefillRate {
		LOW, MEDIUM, HIGH
	};

	double resourceLevel;
	final double maxLevel;
	final RefillRate rate;
	int lastFilled = 0;

	public CommonPool(double initialLevel, double maxLavel, RefillRate rate) {
		super();
		this.resourceLevel = initialLevel;
		this.maxLevel = maxLavel;
		this.rate = rate;
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

	public RefillRate getRate() {
		return rate;
	}

	public int getLastFilled() {
		return lastFilled;
	}

	public void setLastFilled(int lastFilled) {
		this.lastFilled = lastFilled;
	}

	@Override
	public String toString() {
		return "CommonPool [resourceLevel=" + resourceLevel + ", maxLevel="
				+ maxLevel + ", rate=" + rate + "]";
	}

}
