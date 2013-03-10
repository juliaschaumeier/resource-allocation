package allocation.newagents;

import allocation.facts.RaMethod;
import allocation.facts.Profile;

public abstract class Agent {

	final String name;
	final int id;
	final int pool;
	double compliancyDegree;
	final double initialCompliancyDegree;//static?? need it when member->nonMember
	final double standardRequest;
	boolean active = true;
	final Profile profile;
	final RaMethod justicePrAbundance;
	final RaMethod justicePrCrisis;
	final int judgeSize;
	final int judgeTolerance;

	public Agent(String name, int id, int pool, double compliancyDegree, double initialCompliancyDegree, double standardRequest,
			Profile profile, RaMethod justicePrAbundance, RaMethod justicePrCrisis, int judgeSize, int judgeTolerance ) {
		super();
		this.name = name;
		this.id = id;
		this.pool = pool;
		this.compliancyDegree = compliancyDegree;
		this.initialCompliancyDegree = compliancyDegree;
		this.standardRequest = standardRequest;
		this.profile = profile;
		this.justicePrAbundance = justicePrAbundance;
		this.justicePrCrisis = justicePrCrisis;
		this.judgeSize = judgeSize;
		this.judgeTolerance = judgeTolerance;

	}
	
	/**
	 * Copy ctor
	 * @param a
	 */
	public Agent(Agent a) {
		this.name = a.name;
		this.id = a.id;
		this.pool = a.pool;
		this.compliancyDegree = a.compliancyDegree;
		this.initialCompliancyDegree = compliancyDegree;
		this.standardRequest = a.standardRequest;
		this.active = a.active;
		this.profile = a.profile;
		this.justicePrAbundance = a.justicePrAbundance;
		this.justicePrCrisis = a.justicePrCrisis;
		this.judgeSize = a.judgeSize;
		this.judgeTolerance = a.judgeTolerance;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId(){
		return id;
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

	public Profile getProfile() {
		return profile;
	}

	public RaMethod getJusticePrAbundance() {
		return justicePrAbundance;
	}

	public RaMethod getJusticePrCrisis() {
		return justicePrCrisis;
	}

	public int getJudgeSize() {
		return judgeSize;
	}
	
	public int getJudgeTolerance(){
		return judgeTolerance;
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
