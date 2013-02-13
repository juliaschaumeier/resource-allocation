package allocation.newagents;

public abstract class Agent {

	final String name;
	final int pool;
	double compliancyDegree;
	final double initialCompliancyDegree;//static?? need it when member->nonMember
	final double standardRequest;
	double noisePercentage;//where to initialise these, value in simulation.java??
	double noiseLevel;
	boolean active = true;

	public Agent(String name, int pool, double compliancyDegree, double initialCompliancyDegree,
			double standardRequest) {
		super();
		this.name = name;
		this.pool = pool;
		this.compliancyDegree = compliancyDegree;
		this.initialCompliancyDegree = compliancyDegree;
		this.standardRequest = standardRequest;

	}
	
	/**
	 * Copy ctor
	 * @param a
	 */
	public Agent(Agent a) {//what's that for??
		this.name = a.name;
		this.pool = a.pool;
		this.compliancyDegree = a.compliancyDegree;
		this.initialCompliancyDegree = compliancyDegree;
		this.standardRequest = a.standardRequest;
		this.noisePercentage = a.noisePercentage;
		this.noiseLevel = a.noiseLevel;
		this.active = a.active;
	}
	
	public String getName() {
		return name;
	}

	public int getPool() {
		return pool;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public double getCompliancyDegree() {
		return compliancyDegree;
	}

	public double getStandardRequest() {
		return standardRequest;
	}
	
	public double getNoisePercentage() {
		return noisePercentage;
	}

	public double getNoiseLevel() {
		return noiseLevel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Agent other = (Agent) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() +" [" + name + "]";
	}

}
