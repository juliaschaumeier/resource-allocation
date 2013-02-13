package allocation.newagents;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import uk.ac.imperial.presage2.core.util.random.Random;

import allocation.actions.Allocation;
import allocation.actions.CallForVote;
import allocation.actions.Demand;
import allocation.facts.CommonPool;
import allocation.facts.Institution;

public class Head extends Member {

	int monitoring = 0; //set in monitor() used in allocate()
	int outMonitoring = 0;

	public Head(String name, double compliancyDegree, double initialCompliancyDegree, double standardRequest, double noRequestPercentage,
			double changeBehaviourPercentage, double improveBehaviour, int pool, int iid) {
		super(name, compliancyDegree, initialCompliancyDegree, standardRequest, noRequestPercentage, changeBehaviourPercentage, improveBehaviour, pool, iid);
	}

	/**
	 * Promote Member to Head
	 * 
	 * @param m
	 */
	public Head(Member m) {//define stuff to put in head!!
		super(m);
	}

	public CallForVote callForVotes(Institution i) {
		if (i.isPrinciple3()) {
			// vote on ra method, not head
			return new CallForVote(false, true);
		} else {
			return null;
		}
	}

	public Set<Allocation> allocate(Institution i, CommonPool pool,
			List<Demand> demands) { //list automatically created when asked for??
		
		Set<Allocation> allocations = new HashSet<Allocation>();
		//(out)monitoring only !=O if Pr is on
		//what if level is already < 0 => should throw bankrupt here or wait??
		double level = pool.getResourceLevel() - monitoring
				* i.getMonitoringCost() - outMonitoring*i.getOutMonitoringCost();
		if (level < 0){
			//logger.info("Hilfeee, bankrupt!!");
			return allocations;
		}
		switch (i.getAllocationMethod()) {
		case QUEUE:
			Collections.shuffle(demands);
			Queue<Demand> demandQueue = i.getDemandQueue();
			Set<String> alreadyDemanded = new HashSet<String>();
			for (Demand d : demandQueue) {
				alreadyDemanded.add(d.getAgent());
			}
			for (Demand d : demands) {
				if (!alreadyDemanded.contains(d.getAgent()))
					demandQueue.add(d);
			}
			while (!demandQueue.isEmpty()) {
				if (level >= demandQueue.peek().getQuantity()) {
					Demand d = demandQueue.poll();
					allocations.add(new Allocation(i.getRound(), d.getAgent(),
							d.getQuantity()));
					level -= d.getQuantity();
				} else {
					break;
				}
			}
			break;
		case RATION:
			double fairshare = level/demands.size();
			i.setFairshare(fairshare);//need that for later!!
			Collections.shuffle(demands);
			for (Demand d : demands) {
				if (level >= d.getQuantity() || level >= fairshare){
					if(d.getQuantity() > fairshare){
						allocations.add(new Allocation(i.getRound(), d.getAgent(),
								fairshare));
						level -= fairshare;
					}
					else{
						allocations.add(new Allocation(i.getRound(), d.getAgent(),
								d.getQuantity()));
						level -= d.getQuantity();	
					}
				}
				else{
					break;
				}
			}
			break;
		}//switch
		return allocations;
	}

	public Set<String> monitor(Institution i, CommonPool pool,
			Set<Member> members, Set<Agent> nonMembers) {
		Set<String> toMonitor = new HashSet<String>();
		monitoring = 0;
		outMonitoring = 0;
		// member monitoring
		if (i.isPrinciple4()) {
			for (Member ag : members) {
				if (ag.active && Random.randomDouble() < i.getMonitoringLevel()) {
					toMonitor.add(ag.getName());
					monitoring++;
				}
			}
		}
		if (i.isPrinciple1()) {
			for (Agent ag : nonMembers) {
				if (ag.active
						&& Random.randomDouble() < i.getOutMonitoringLevel()) {
					toMonitor.add(ag.getName());
					outMonitoring++;
				}
			}
		}
		return toMonitor;
	}

}
