package allocation.agents;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import allocation.actions.Allocation;
import allocation.actions.CallForVote;
import allocation.actions.Demand;
import allocation.facts.CommonPool;

/**
 * Defines the behaviour of the head agent
 * 
 */
class HeadBehaviour extends MemberBehaviour {

	HeadBehaviour(Agent self) {
		super(self);
	}

	@Override
	public void doBehaviour() {
		switch (self.institution.getState()) {
		case CFV:
			cfv();
			break;
		default:
			super.doBehaviour();
			break;
		}
	}

	void assign() {
	}

	void exclude() {
	}

	void eliminate() {
	}

	void report() {
	}

	void reportOut() {
	}

	void cfv() {
		if (self.institution.isPrinciple3()) {
			// call for vote on ra method
			self.act(new CallForVote(false, true));
		}
	}

	void declare() {
	}

	Set<Allocation> allocate(CommonPool pool, List<Demand> demands) {
		Set<Allocation> allocations = new HashSet<Allocation>();
		switch (self.institution.getAllocationMethod()) {
		case QUEUE:
			Collections.shuffle(demands);
			Queue<Demand> demandQueue = self.institution.getDemandQueue();
			Set<Agent> alreadyDemanded = new HashSet<Agent>();
			for (Demand d : demandQueue) {
				alreadyDemanded.add(d.getPlayer());
			}
			for (Demand d : demands) {
				if (!alreadyDemanded.contains(d.getPlayer()))
					demandQueue.add(d);
			}
			double level = pool.getResourceLevel();
			while (!demandQueue.isEmpty()) {
				if (level > demandQueue.peek().getQuantity()) {
					Demand d = demandQueue.poll();
					allocations.add(new Allocation(self.institution.getRound(),
							d.getPlayer(), d.getQuantity()));
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

	void sanction() {
	}

	void uphold() {
	}

}
