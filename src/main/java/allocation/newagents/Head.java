package allocation.newagents;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import allocation.actions.Allocation;
import allocation.actions.CallForVote;
import allocation.actions.Demand;
import allocation.agents.Agent;
import allocation.facts.CommonPool;
import allocation.facts.Institution;

public class Head extends Member {

	int monitoring = 0;
	int outMonitoring = 0;

	public Head(String name, double compliancyDegree, double standardRequest,
			int pool, int iid) {
		super(name, compliancyDegree, standardRequest, pool, iid);
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
			// vote ra method
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

}
