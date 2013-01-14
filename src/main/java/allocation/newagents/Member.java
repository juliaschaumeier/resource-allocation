package allocation.newagents;

import uk.ac.imperial.presage2.core.util.random.Random;
import allocation.actions.Allocation;
import allocation.actions.Demand;
import allocation.actions.Vote;
import allocation.facts.CommonPool;
import allocation.facts.Institution;
import allocation.facts.RaMethod;

public class Member extends Agent {

	int institutionId;
	final double preferredRequest;
	final double noRequestPercentage;

	public Member(String name, double compliancyDegree, double standardRequest, double noRequestPercentage,
			int pool, int iid) {
		super(name, pool, compliancyDegree, standardRequest);
		// oscillate with an amplitude of 0.1
		this.preferredRequest = standardRequest * compliancyDegree
				* (1 + (0.2 * Random.randomDouble() - 0.1));
		this.noRequestPercentage = noRequestPercentage;
		this.institutionId = iid;
	}

	/**
	 * Copy ctr
	 * 
	 * @param m
	 */
	public Member(Member m) {
		super(m);
		this.institutionId = m.institutionId;
		this.preferredRequest = m.preferredRequest;
		this.noRequestPercentage = m.noRequestPercentage;
	}

	/**
	 * Convert a NonMember into a Member
	 * 
	 * @param nm
	 * @param iid
	 */
	public Member(NonMember nm, int iid, double noRequestPercentage) {
		super(nm);
		this.preferredRequest = standardRequest * compliancyDegree
				* (1 + (0.2 * Random.randomDouble() - 0.1));
		this.institutionId = iid;
		this.noRequestPercentage = noRequestPercentage;
	}

	/**
	 * Get appropriation for this agent
	 * 
	 * @param i
	 *            Agent's institution
	 * @param pool
	 *            Agent's pool
	 * @param all
	 *            Allocation for this agent (if applicable)
	 * @return Quantity to appropriate this round
	 */
	public double appropriate(Institution i, CommonPool pool, Allocation all) {
		if (active) {
			double appropriateAmount = 0;
			if (i.isPrinciple2()) {
				if (all == null) {
					appropriateAmount = 0;
				} else {
					appropriateAmount = all.getQuantity();
				}
			} else {
				// principle 2 disabled: use preferredRequest
				appropriateAmount = preferredRequest;
			}

			// do appropriation action
			return appropriateAmount;
		}
		return 0;
	}
	
	public Vote vote(Institution i, CommonPool pool, String ballot) {
		if (ballot.equals("raMethod") && i.isVoteRaMethod()) {
			RaMethod vote;
			if (pool.getResourceLevel() < 1.5 * i.getInitialAgents()
					* standardRequest / compliancyDegree) {
				vote = RaMethod.RATION;
			} else {
				vote = RaMethod.QUEUE;
			}
			return Vote.voteRaMethod(vote);
		}
		return null;
	}

	public double demand(Institution i, CommonPool pool) {
		//agents do not demand in every round
		if (active && i.isPrinciple2() && Random.randomDouble() > noRequestPercentage) {
			if( i.getAllocationMethod() == RaMethod.QUEUE){
				return preferredRequest;
			}
			if(i.getAllocationMethod() == RaMethod.RATION){
				/*if(count members....){
					if/else
				}
				else {
					return preferredRequest;
				}*/
				
			}
		}
		
		
		return 0;
	}

	public int getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(int institutionId) {
		this.institutionId = institutionId;
	}

}
