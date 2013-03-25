package allocation.facts;



public class CommonPool {

	final int id;
	double resourceLevel;
	double maxLevel;
	int lastFilled = -1;
	boolean depleated = false;
	double outAppropriationFrequency; //depends on pool (and institution) how hard it is to appropriate from it
	double outImproveFrequency;
	double startResourceLevel ; //level at start of each round
	RefillScheme refScheme;
	
	public CommonPool(int id, double initialLevel, double maxLevel, double outAppropriationFrequency, double outImproveFrequency, RefillScheme refScheme) {
		super();
		this.id = id;
		this.resourceLevel = initialLevel;
		this.startResourceLevel = initialLevel;
		this.maxLevel = maxLevel;
		this.outAppropriationFrequency = outAppropriationFrequency;
		this.outImproveFrequency = outImproveFrequency;
		this.refScheme = refScheme;
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

	public void setMaxLevel(double maxLevel) {
		this.maxLevel = maxLevel;
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

	public double getOutAppropriationFrequency() {
		return outAppropriationFrequency;
	}

	public void setOutAppropriationFrequency(double outAppropriationFrequency) {
		this.outAppropriationFrequency = outAppropriationFrequency;
	}

	public double getOutImproveFrequency() {
		return outImproveFrequency;
	}

	public double getStartResourceLevel() {
		return startResourceLevel;
	}

	public void setStartResourceLevel(double startResourceLevel) {
		this.startResourceLevel = startResourceLevel;
	}

	public RefillScheme getRefScheme(){
		return refScheme;
	}
	
}
