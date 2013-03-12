package allocation.newagents;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import uk.ac.imperial.presage2.core.util.random.Random;

import allocation.actions.Allocation;
import allocation.actions.CallForVote;
import allocation.actions.Demand;
import allocation.facts.CommonPool;
import allocation.facts.Institution;
import allocation.facts.RaMethod;
import allocation.facts.Profile;

public class Head extends Member {

	int monitoring = 0; //set in monitor() used in allocate()
	int outMonitoring = 0;

	public Head(String name, int id, double compliancyDegree, double initialCompliancyDegree, double standardRequest, double noRequestPercentage,
			double changeBehaviourPercentage, double improveBehaviour, int pool, int iid,
			Profile profile, RaMethod justicePrAbundance, RaMethod justicePrCrisis, int judgeSize, int judgeTolerance,
			boolean useSat, double initialSat, double leaveSat, double increaseSat, double decreaseSat) {
		super(name, id, compliancyDegree, initialCompliancyDegree, standardRequest, noRequestPercentage, 
				changeBehaviourPercentage, improveBehaviour, pool, iid, profile, justicePrAbundance, justicePrCrisis, 
				judgeSize, judgeTolerance, useSat, initialSat, leaveSat, increaseSat, decreaseSat );
	}

	/**
	 * Promote Member to Head
	 * 
	 * @param m
	 */
	public Head(Member m) {
		super(m);
	}

	public CallForVote callForVotes(Institution i) {
		if (i.isPrinciple3()) {
			// vote on (head, raMethod) could be chosen with a method
			return new CallForVote(i.isVoteHead(), i.isVoteRaMethod());
		} else {
			return null;
		}
	}

	public Set<Allocation> allocate(Institution i, CommonPool pool,
			List<Demand> demands) { 
		
		Set<Allocation> allocations = new HashSet<Allocation>();
		//(out)monitoring only !=O if Pr is on
		double level = pool.getResourceLevel() - monitoring
				* i.getMonitoringCost() - outMonitoring*i.getOutMonitoringCost();
		if (level < 0){
			//logger.info("Hilfeee, bankrupt!!");
			return allocations;
		}
		
		if(i.isHeadDecides()==false){//use RaMethod that's been voted for or externally decided
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
				int qSize = demandQueue.size();
				System.out.println("Queue Q " + demandQueue );
				int qCounter = 0; //loop through demandQueue only once
				while (!demandQueue.isEmpty() && qCounter < qSize) {
					qCounter ++;
					if (level >= demandQueue.peek().getQuantity()) {
						Demand d = demandQueue.poll();
						allocations.add(new Allocation( d.getAgent(), i.getRound(),
								d.getQuantity(), pool.getId()));
						level -= d.getQuantity();
					} else {
						break;
					}
				}
				break;
			case RATION:
				double fairshare = level/demands.size();
				i.setFairshare(fairshare);//need that for later
				Collections.shuffle(demands);
				for (Demand d : demands) {
					if (level >= d.getQuantity() || level >= fairshare){
						if(d.getQuantity() > fairshare){
							allocations.add(new Allocation(d.getAgent(), i.getRound(), 
									fairshare, pool.getId()));
							level -= fairshare;
						}
						else{
							allocations.add(new Allocation(d.getAgent(), i.getRound(),
									d.getQuantity(), pool.getId()));
							level -= d.getQuantity();	
						}
					}
					else{
						break;
					}
				}
				break;
			default:
				break;
			}//end switch
		
		} else {//headDecides==true, even if head is set externally
			RaMethod justicePr;
			LinkedList<Demand> demandQueue = new LinkedList<Demand>();
			if (level < 1.5 * i.getInitialAgents()* standardRequest / compliancyDegree) {//low resource
				justicePr = justicePrCrisis;
			} else {
				justicePr = justicePrAbundance;
			}
			System.out.println("justicePr of head:  " + justicePr);
			switch(justicePr){
			case EQUITY:
				Collections.shuffle(demands);
				for (Demand d : demands){
					if (d.getProfile()==Profile.MERITIOUS){
						demandQueue.addFirst(d);
					} else {
						demandQueue.addLast(d);
					}
				}
				System.out.println("Equity Q " + demandQueue );
				break;
				
			case EQUALITY:
				Collections.shuffle(demands);
				for(Demand d : demands){
					demandQueue.add(d);
				}
				System.out.println("Equality Q " + demandQueue );
				break;
			case NEED:
				Collections.shuffle(demands);
				for (Demand d : demands){
					if (d.getProfile()==Profile.NEEDY){
						demandQueue.addFirst(d);
					} else {
						demandQueue.addLast(d);
					}
				}
				System.out.println("Need Q " + demandQueue );
				break;
			default:
				break;			
			}//end switch
			int qSize = demandQueue.size();
			int qCounter = 0; //loop through demandQueue only once
			while (!demandQueue.isEmpty() && qCounter < qSize) {
				qCounter ++;
				if (level >= demandQueue.peek().getQuantity()) {
					Demand d = demandQueue.poll();
					allocations.add(new Allocation( d.getAgent(), i.getRound(),
							d.getQuantity(), pool.getId()));
					level -= d.getQuantity();
				} else {
					break;
				}
			}
			
		}//end else
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
		// nonmember monitoring
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
