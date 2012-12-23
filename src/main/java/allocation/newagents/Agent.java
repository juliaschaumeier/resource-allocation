package allocation.newagents;

public abstract class Agent {

	final String name;
	final int pool;
	final double compliancyDegree;
	final double standardRequest;
	boolean active = true;

	public Agent(String name, int pool, double compliancyDegree,
			double standardRequest) {
		super();
		this.name = name;
		this.pool = pool;
		this.compliancyDegree = compliancyDegree;
		this.standardRequest = standardRequest;
	}
	
	/**
	 * Copy ctor
	 * @param a
	 */
	public Agent(Agent a) {
		this.name = a.name;
		this.pool = a.pool;
		this.compliancyDegree = a.compliancyDegree;
		this.standardRequest = a.standardRequest;
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

}
