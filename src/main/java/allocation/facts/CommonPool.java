package allocation.facts;



public class CommonPool {

	public enum RefillRate {
		LOW, MEDIUM, HIGH
	};

	final int id;
	double resourceLevel;
	final double maxLevel;
	int lastFilled = 0;
	boolean depleated = false;

	public CommonPool(int id, double initialLevel, double maxLavel) {
		super();
		this.id = id;
		this.resourceLevel = initialLevel;
		this.maxLevel = maxLavel;
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

	public boolean isDepleated() {
		return depleated;
	}

	@Override
	public String toString() {
		return "CommonPool " + id + " [resourceLevel=" + resourceLevel
				+ ", maxLevel=" + maxLevel + "]";
	}

}
