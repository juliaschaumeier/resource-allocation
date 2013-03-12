package allocation.newagents;

import uk.ac.imperial.presage2.core.util.random.Random;
import allocation.actions.Allocation;
import allocation.actions.Demand;
import allocation.actions.Vote;
import allocation.facts.CommonPool;
import allocation.facts.Institution;
import allocation.facts.RaMethod;
import allocation.facts.Profile;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
//import allocation.DroolsSimulation;


public class Member extends Agent {

	int institutionId;
	double preferredRequest;
	final double noRequestPercentage;
	final double changeBehaviourPercentage;
	final double improveBehaviour;
	List<String> droppedHeads; //remembers list when becomes Head, doesn't when it becomes nonMember
	
	double satisfaction; //remembers satisfaction when becomes Head, doesn't when it becomes nonMember
	final boolean useSat;
	final double initialSat;
	final double leaveSat;
	final double increaseSat;
	final double decreaseSat;

	double demand = 0;

	public Member(String name, int id, double compliancyDegree, double initialCompliancyDegree,
			double standardRequest, double noRequestPercentage, double changeBehaviourPercentage,
			double improveBehaviour, int pool, int iid, Profile profile, 
			RaMethod justicePrAbundance, RaMethod justicePrCrisis, int judgeSize, int judgeTolerance,
			boolean useSat, double initialSat, double leaveSat, double increaseSat, double decreaseSat ) {
		super(name, id, pool, compliancyDegree, initialCompliancyDegree, standardRequest,
				profile, justicePrAbundance, justicePrCrisis, judgeSize, judgeTolerance);
		this.preferredRequest = standardRequest * compliancyDegree
				* (1 + (0.2 * Random.randomDouble() - 0.1));
		this.noRequestPercentage = noRequestPercentage;
		this.changeBehaviourPercentage = changeBehaviourPercentage;
		this.improveBehaviour = improveBehaviour;
		this.institutionId = iid;
		this.droppedHeads = new ArrayList<String>();
		this.satisfaction = initialSat;
		this.useSat = useSat;
		this.initialSat = initialSat;
		this.leaveSat = leaveSat;
		this.increaseSat = increaseSat;
		this.decreaseSat = decreaseSat;
		
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
		this.droppedHeads = m.droppedHeads;
		this.satisfaction = m.initialSat;
		this.useSat = m.useSat;
		this.initialSat = m.initialSat;
		this.leaveSat = m.leaveSat;
		this.increaseSat = m.increaseSat;
		this.decreaseSat = m.decreaseSat;
	}

	/**
	 * Convert a NonMember into a Member
	 * 
	 * @param nm
	 * @param iid
	 */
	public Member(NonMember nm, int iid, double noRequestPercentage, double changeBehaviourPercentage, double improveBehaviour,
			boolean useSat, double initialSat, double leaveSat,  double increaseSat, double decreaseSat) {
		super(nm);
		this.preferredRequest = standardRequest * compliancyDegree
				* (1 + (0.2 * Random.randomDouble() - 0.1));
		this.institutionId = iid;
		this.noRequestPercentage = noRequestPercentage;
		this.changeBehaviourPercentage = changeBehaviourPercentage;
		this.improveBehaviour = improveBehaviour;
		this.droppedHeads = new ArrayList<String>(); 
		this.satisfaction = initialSat;
		this.useSat = useSat;
		this.initialSat = initialSat;
		this.leaveSat = leaveSat;
		this.increaseSat = increaseSat;
		this.decreaseSat = decreaseSat;
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
		if(ballot.equals("head")){
			Member vote;
			List<Member> helplist = new ArrayList<Member>();
			for (Object o : i.getInstMembers()){
				helplist.add((Member) o);
			}
			//didn't dislike head end of last round, vote for same head
			if (!droppedHeads.contains(i.getInstHead().getName())){
				vote = i.getInstHead();
				
			} else {
				for (Object o : i.getInstMembers()){
					Member m = (Member) o;
					if (droppedHeads.contains(m.getName())){
						helplist.remove(m);
					}
				}
				//System.out.println("helplist " + helplist);
				if(helplist.isEmpty()){
					vote = null;
				} else {
					vote = (Member) helplist.get(Random.randomInt(helplist.size()));
				}
			}
			//vote is null if no heads left
			return Vote.voteHead(vote);
		}	
		//no vote??
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
			default:
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

			if(i.isUnintentionalError() && Random.randomDouble() < i.getNoisePercentage()){//not every agent subject to noise
				double share = standardRequest;
				if( i.isPrinciple2() && i.getAllocationMethod() == RaMethod.RATION ){
					share = i.getFairshare();
				}
				if(Random.randomDouble() < 0.5){
					appropriateAmount += share*i.getNoiseLevel()*Random.randomDouble(); 
					System.out.println("oups too much " + this.name);
				}
				else{
					appropriateAmount -= share*i.getNoiseLevel()*Random.randomDouble();
					System.out.println("oups too little " + this.name);
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
	
	public List<String> judgeSelect(List<String> acNames){
		List<String> copyNames = new ArrayList<String>();
		for(String s : acNames){
			copyNames.add(s);
		}
		if(copyNames.size() > judgeSize){
			Collections.shuffle(copyNames);
			copyNames = copyNames.subList(0, judgeSize);
		}
		return copyNames;
	}
	
	public void judgeHead(Institution i, CommonPool pool, Head head, List<Demand> demands, List<Allocation> allocations){
		RaMethod justicePr;
		if (pool.getStartResourceLevel() < 1.5 * i.getInitialAgents()
				* standardRequest / compliancyDegree) {//low resource
			justicePr = justicePrCrisis;
		} else {
			justicePr = justicePrAbundance;
		}
		//how many agents can get allocated roughly:
		int helpalloc = (int) ((pool.getStartResourceLevel()/standardRequest)*((double) judgeSize)/i.getInitialAgents()); //=floor
		int meritiousDem = 0;
		int needyDem = 0;
		int meritiousAll = 0;
		int needyAll = 0;
		for (Demand d : demands){
			if (d.getProfile()== Profile.NEEDY){
				needyDem ++;
				for (Allocation a : allocations){//can be shorter???
					if (d.getAgent()==a.getAgent()){
						needyAll ++;
					}
				}
			} else { //profile==MERITIOUS
				meritiousDem ++;
				for (Allocation a : allocations){
					if (d.getAgent()==a.getAgent()){
						meritiousAll ++;
					}
				}
			}
			
		}
		System.out.println("Before: ha " + helpalloc + ", mD=" + meritiousDem + ", nD=" + needyDem + ", mA=" + meritiousAll + ", nA=" + needyAll + ", justicePr=" + justicePr );
		switch (justicePr){
		case EQUITY:
			//how many should get allocated with this agent's profile
			if(helpalloc > meritiousDem){
				if(helpalloc-meritiousDem < needyDem){
					needyDem = helpalloc-meritiousDem;
				}//else both fully allocated
			} else {
				meritiousDem = helpalloc;
				needyDem = 0;
			}
			break;
		case EQUALITY:
			//allocate according to percentage demands wrt both groups
			double demSum = (double) needyDem + meritiousDem;
			if(helpalloc < demSum){//not enough resource to go round
				needyDem = (int) (helpalloc*needyDem/demSum + 0.5); //0.5 for rounding
				meritiousDem = helpalloc - needyDem; //(int) (helpalloc*meritiousDem/demSum + 0.5);
			}//else fully allocated
			break;
		case NEED:
			//how many should get allocated with this agent's profile
			if(helpalloc > needyDem){
				if(helpalloc-needyDem < meritiousDem){
					meritiousDem = helpalloc-needyDem;
				}//else both fully allocated
			} else {
				needyDem = helpalloc;
				meritiousDem = 0;
			}
			break;
		default:
			break;
		}//end of switch

//		System.out.println("after: ha " + helpalloc + ", mD=" + meritiousDem + ", nD=" + needyDem + ", mA=" + meritiousAll + ", nA=" + needyAll );
		//test whether allocation within tolerance range of demand(=agent's allocation)
		if (meritiousAll < meritiousDem - judgeTolerance || meritiousAll > meritiousDem + judgeTolerance){
			if(!droppedHeads.contains(head.getName())){//with useSat tests head in every timestep
				droppedHeads.add(head.getName());
			}
			if(useSat){//head naughty, satisfaction decreases
				satisfaction -= satisfaction*decreaseSat;
			}
			
			System.out.println("after: ha " + helpalloc + ", mD=" + meritiousDem + ", nD=" + needyDem + ", mA=" + meritiousAll + ", nA=" + needyAll );
			System.out.println("naughty head (m)" + head.getName() + head.profile + " by member [" + name + "]" + profile );
			return;
		} else if (needyAll < needyDem - judgeTolerance || needyAll > needyDem + judgeTolerance){
			if(!droppedHeads.contains(head.getName())){
				droppedHeads.add(head.getName());
			}
			if(useSat){//head naughty, satisfaction decreases
				satisfaction -= satisfaction*decreaseSat;
			}
			System.out.println("naughty head (n)" + head.getName() + head.profile + " by member [" + name + "]" + profile );
			System.out.println("after: ha " + helpalloc + ", mD=" + meritiousDem + ", nD=" + needyDem + ", mA=" + meritiousAll + ", nA=" + needyAll );
			return;
		} else {
			if(useSat){//head nice, satisfaction increases
				satisfaction += (1-satisfaction)*increaseSat;
			}
			return;
		}
		
	}


	public int getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(int institutionId) {
		this.institutionId = institutionId;
	}

	public List<String> getDroppedHeads() {
		return droppedHeads;
	}
	
	public double getSatisfaction() {
		return satisfaction;
	}

	public void setSatisfaction(double satisfaction) {
		this.satisfaction = satisfaction;
	}

	public boolean isUseSat() {
		return useSat;
	}

	public double getInitialSat() {
		return initialSat;
	}

	public double getLeaveSat() {
		return leaveSat;
	}

	public double getIncreaseSat() {
		return increaseSat;
	}

	public double getDecreaseSat() {
		return decreaseSat;
	}
	
	
}
