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

	int monitoring = 0;
	int outMonitoring = 0;

	public Head(String name, double compliancyDegree, double standardRequest, double noRequestPercentage,
			int pool, int iid) {
		super(name, compliancyDegree, standardRequest, noRequestPercentage, pool, iid);
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
			// vote on ra method, not head
			return new CallForVote(false, true);
		} else {
			return null;
		}
	}

	public Set<Allocation> allocate(Institution i, CommonPool pool,
			List<Demand> demands) {
		Set<Allocation> allocations = new HashSet<Allocation>();
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
			double level = pool.getResourceLevel() - monitoring
					* i.getMonitoringCost();
			while (!demandQueue.isEmpty()) {
				if (level > demandQueue.peek().getQuantity()) {
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
			break;
		}
		return allocations;
	}

	public Set<String> monitor(Institution i, CommonPool pool,
			Set<Member> members, Set<Agent> nonMembers) {
		Set<String> toMonitor = new HashSet<String>();
		// member monitoring
		if (i.isPrinciple4()) {
			monitoring = 0;
			for (Member ag : members) {
				if (ag.active && Random.randomDouble() < i.getMonitoringLevel()) {
					toMonitor.add(ag.getName());
					monitoring++;
				}
			}
		}
		if (i.isPrinciple1()) {
			outMonitoring = 0;
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
