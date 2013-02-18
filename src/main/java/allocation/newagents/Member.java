package allocation.newagents;

import uk.ac.imperial.presage2.core.util.random.Random;
import allocation.actions.Allocation;
import allocation.actions.Vote;
import allocation.facts.CommonPool;
import allocation.facts.Institution;
import allocation.facts.RaMethod;

public class Member extends Agent {

	int institutionId;
	double preferredRequest;
	final double noRequestPercentage;
	final double changeBehaviourPercentage;
	final double improveBehaviour;
	
	double demand = 0;

	public Member(String name, double compliancyDegree, double initialCompliancyDegree, double standardRequest, double noRequestPercentage,
			double changeBehaviourPercentage, double improveBehaviour, int pool, int iid) {
		super(name, pool, compliancyDegree, initialCompliancyDegree, standardRequest);
		// oscillate with an amplitude of 0.1
		this.preferredRequest = standardRequest * compliancyDegree
				* (1 + (0.2 * Random.randomDouble() - 0.1));
		this.noRequestPercentage = noRequestPercentage;
		this.changeBehaviourPercentage = changeBehaviourPercentage;
		this.improveBehaviour = improveBehaviour;
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
		this.changeBehaviourPercentage = m.changeBehaviourPercentage;
		this.improveBehaviour = m.improveBehaviour;
	}

	/**
	 * Convert a NonMember into a Member
	 * 
	 * @param nm
	 * @param iid
	 */
	public Member(NonMember nm, int iid, double noRequestPercentage, double changeBehaviourPercentage, double improveBehaviour) {
		super(nm);
		this.preferredRequest = standardRequest * compliancyDegree
				* (1 + (0.2 * Random.randomDouble() - 0.1));
		this.institutionId = iid;
		this.noRequestPercentage = noRequestPercentage;
		this.changeBehaviourPercentage = changeBehaviourPercentage;
		this.improveBehaviour = improveBehaviour;
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
		//agents do not demand in every round, in demandQueue tested in 'member demand'
		if (active && i.isPrinciple2() && Random.randomDouble() > noRequestPercentage) {
			switch (i.getAllocationMethod()) {
			case QUEUE:
					demand = preferredRequest;
					break;
			case RATION:
					if(pool.getResourceLevel()*compliancyDegree/i.getActiveMemberCount() < preferredRequest){
						if(!i.isPrinciple4()){//no monitoring
							demand = pool.getResourceLevel()*compliancyDegree/i.getActiveMemberCount();
						}
						else {//with monitoring (outMonitoring not take into account_)
							demand = (pool.getResourceLevel() - i.getActiveMemberCount()*i.getMonitoringCost()
									*i.getMonitoringLevel())*compliancyDegree/i.getActiveMemberCount();
						}
					}
					else {
						demand = preferredRequest;
					}
					break;
			}//switch
		}
		
		else demand = 0;
		
		if(demand < 0){
			demand = 0;
		}
		
		return demand;
	}
	
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

			if(i.isUnintentionalError() && Random.randomDouble() < noisePercentage){//not every agent subject to noise
				double share = standardRequest;
				if( i.isPrinciple2() && i.getAllocationMethod() == RaMethod.RATION ){
					share = i.getFairshare();
				}
				if(Random.randomDouble() < 0.5){
					appropriateAmount += share*noiseLevel*Random.randomDouble(); 
				}
				else{
					appropriateAmount -= share*noiseLevel*Random.randomDouble();
				}
			}
			if (appropriateAmount < 0){
				appropriateAmount = 0;
			}
			return appropriateAmount;
		}
		//inactive agents not subjective to noise
		else {
			return 0;
		}
	}
	
	public void changeBehaviour(int maxSanctionLevel, int memberLevel){
		if(Random.randomDouble() < changeBehaviourPercentage + memberLevel/(2*maxSanctionLevel)){
			compliancyDegree -= compliancyDegree*(1 - improveBehaviour*Random.randomDouble());
			preferredRequest = standardRequest*compliancyDegree*(0.9 + 0.2*Random.randomDouble());
		}
		//else behaviour stays the same
	}

	public int getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(int institutionId) {
		this.institutionId = institutionId;
	}

}
