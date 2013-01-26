package allocation.newagents;

import uk.ac.imperial.presage2.core.util.random.Random;
import allocation.actions.Allocation;
import allocation.actions.Vote;
import allocation.facts.CommonPool;
import allocation.facts.Institution;
import allocation.facts.RaMethod;

public class Member extends Agent {

	int institutionId;
	final double preferredRequest;
	final double noRequestPercentage;
	
	double demand = 0;

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
				double amount = (all == null ? 0 : all.getQuantity());
				if (compliancyDegree > 1) {
					//appropriate more than allowed (only top up, if allocated small)
					if(amount + (preferredRequest-standardRequest) < demand){
						appropriateAmount = amount + (preferredRequest-standardRequest);
					}
					else appropriateAmount = demand;
				}
				else{
					//appropriate allocation
					appropriateAmount = amount;
				}
			} else if (Random.randomDouble() > noRequestPercentage) {
				// principle 2 disabled: use preferredRequest
				appropriateAmount = preferredRequest;
			}

			// do appropriation action (if noise, shake up here)
			return appropriateAmount;
		}
		//inactive agents not subjective to noise
		else {
			return 0;
		}
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
				demand = preferredRequest;
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
		
		demand = 0;
		return 0;
	}

	public int getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(int institutionId) {
		this.institutionId = institutionId;
	}

}
