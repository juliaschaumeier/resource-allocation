package allocation.facts;

import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.ArrayList;

import org.drools.runtime.StatefulKnowledgeSession;

import allocation.Phase;
import allocation.actions.Demand;
import allocation.newagents.Member;
import allocation.newagents.Head;

public class Institution {

	final StatefulKnowledgeSession session;

	final int id;
	Phase state = Phase.CFV;
	int round = 0;

	final boolean principle1 = true;
	boolean principle2 = false;
	boolean principle3 = false;
	boolean principle4 = false;
	boolean principle5 = false;
	boolean principle6 = false;
	boolean unintentionalError = false;

	final int initialAgents;
	boolean voteHead = false; //was set in every timestep before ('Disable votes'), changed that.
	boolean voteRaMethod = false;
	final boolean headDecides;
	//int memberCount = 0;
	int activeMemberCount = 0;
	final int samplingrateRaMethod; //external agent decides every x timesteps about RaMethod
	final int samplingrateHead;

	RaMethod allocationMethod = RaMethod.QUEUE;
	final double monitoringLevel;
	final double monitoringCost;
	final double outMonitoringLevel;
	final double outMonitoringCost;
	final double noisePercentage;
	final double noiseLevel;
	
	double fairshare = 50; // for RaMethod=RATION; set in head.allocate()	
	final int appealtime; //how long no offence to be let off
	int maxSanctionLevel = 3; //for graduated sanctions
	int excludetime = 5;//multiplied with sanction level
	
	final CommonPool pool;

	public Queue<Demand> demandQueue = new LinkedList<Demand>();
	public List<Member> instMembers = new ArrayList<Member>();
	public Head instHead;

	public Institution(StatefulKnowledgeSession session, int id,
			int initialAgents, CommonPool pool, boolean principle2, boolean principle3,
			boolean principle4, boolean principle5, boolean principle6, boolean unintentionalError,
			boolean voteHead, boolean voteRaMethod, boolean headDecides, double monitoringLevel, double monitoringCost, 
			double outMonitoringLevel, double outMonitoringCost, double noisePercentage,
			double noiseLevel, int appealtime, int samplingrateRaMethod, int samplingrateHead) {
		super();
		this.session = session;
		this.id = id;
		this.pool = pool;
		this.principle2 = principle2;
		this.principle3 = principle3;//voting
		this.principle4 = principle4;
		this.principle5 = principle5;
		this.principle6 = principle6;
		this.unintentionalError = unintentionalError;
		this.voteHead = voteHead;
		this.voteRaMethod = voteRaMethod;
		this.headDecides = headDecides;
		this.initialAgents = initialAgents;
		this.monitoringLevel = monitoringLevel;
		this.monitoringCost = monitoringCost;
		this.outMonitoringLevel = outMonitoringLevel;
		this.outMonitoringCost = outMonitoringCost;
		this.noisePercentage = noisePercentage;
		this.noiseLevel = noiseLevel;
		this.appealtime = appealtime;
		this.samplingrateRaMethod = samplingrateRaMethod;
		this.samplingrateHead = samplingrateHead;
	}

	public int getId() {
		return id;
	}

	public CommonPool getPool() {
		return pool;
	}

	public boolean isPrinciple1() {
		return principle1;
	}

	public boolean isPrinciple2() {
		return principle2;
	}

	public boolean isPrinciple3() {
		return principle3;
	}

	public boolean isPrinciple4() {
		return principle4;
	}

	public boolean isPrinciple5() {
		return principle5;
	}

	public boolean isPrinciple6() {
		return principle6;
	}
	
	public boolean isUnintentionalError(){
		return unintentionalError;
	}

	public Phase getState() {
		return state;
	}

	public void setState(Phase state) {
		this.state = state;
	}

	public int getRound() {
		return round;
	}

	public void incrementRound() {
		++round;
	}

	public int getInitialAgents() {
		return initialAgents;
	}

	public boolean isVoteHead() {
		return voteHead;
	}

	public boolean isVoteRaMethod() {
		return voteRaMethod;
	}

	public boolean isHeadDecides() {
		return headDecides;
	}

	public void setVoteHead(boolean voteHead) {
		this.voteHead = voteHead;
	}

	public void setVoteRaMethod(boolean voteRaMethod) {
		this.voteRaMethod = voteRaMethod;
	}

	public RaMethod getAllocationMethod() {
		return allocationMethod;
	}

	public void setAllocationMethod(RaMethod allocationMethod) {
		this.allocationMethod = allocationMethod;
	}

	public double getFairshare() {
		return fairshare;
	}

	public void setFairshare(double fairshare) {
		this.fairshare = fairshare;
	}

	public int getMaxSanctionLevel() {
		return maxSanctionLevel;
	}

	public int getExcludetime() {
		return excludetime;
	}

	@Override
	public String toString() {
		return "Institution [id=" + id + ", state=" + state + ", round="
				+ round + ", allocationMethod=" + allocationMethod + "]";
	}

	public Queue<Demand> getDemandQueue() {
		return demandQueue;
	}
	
	public List<Member> getInstMembers() {
		return instMembers;
	}

	public void setInstMembers(List<Member> instMembers) {
		this.instMembers = instMembers;
	}

	public Head getInstHead() {
		return instHead;
	}

	public void setInstHead(Head instHead) {
		this.instHead = instHead;
	}
	
//	public int getMemberCount() {
//		return memberCount;
//	}

//	public void setMemberCount(int memberCount) {
//		this.memberCount = memberCount;
//	}

	public int getActiveMemberCount() {
		return activeMemberCount;
	}

	public void setActiveMemberCount(int activeMemberCount) {
		this.activeMemberCount = activeMemberCount;
	}

	public int getSamplingrateRaMethod() {
		return samplingrateRaMethod;
	}
	
	public int getSamplingrateHead() {
		return samplingrateHead;
	}

	public double getMonitoringLevel() {
		return monitoringLevel;
	}

	public double getMonitoringCost() {
		return monitoringCost;
	}

	public double getOutMonitoringLevel() {
		return outMonitoringLevel;
	}

	public double getOutMonitoringCost() {
		return outMonitoringCost;
	}

	public double getNoisePercentage() {
		return noisePercentage;
	}

	public double getNoiseLevel() {
		return noiseLevel;
	}

	public int getAppealtime() {
		return appealtime;
	}
}
