package allocation.agents;

import allocation.actions.Appropriate;
import allocation.actions.Demand;
import allocation.actions.Vote;
import allocation.facts.RaMethod;

class MemberBehaviour extends AgentBehaviour {

	MemberBehaviour(Agent self) {
		super(self);
	}

	@Override
	public void doBehaviour() {
		switch (self.institution.getState()) {
		case Appropriate:
			appropriate();
			break;
		case Vote:
			vote();
			break;
		case Demand:
			demand();
			break;
		default:
			break;
		}
	}

	void vote() {
		if (self.institution.isVoteRaMethod()) {
			RaMethod vote;
			if (self.poolMonitor.getResourceLevel() < 1.5
					* self.institution.getInitialAgents()
					* self.standardRequest / self.compliancyDegree) {
				vote = RaMethod.RATION;
			} else {
				vote = RaMethod.QUEUE;
			}
			self.act(Vote.voteRaMethod(vote));
		}
		if (self.institution.isVoteHead()) {
			// head vote here
		}
	}

	void demand() {
		if (self.active && self.institution.isPrinciple2()) {
			self.act(new Demand(self.pool, self.preferredRequest));
		}
	}

	@Override
	void appropriate() {
		if (self.active) {
			double appropriateAmount = 0;
			if (self.institution.isPrinciple2()) {
				if (self.compliancyDegree > 1) {
					
				}
			} else {
				// principle 2 disabled: use preferredRequest
				appropriateAmount = self.preferredRequest;
			}

			// do appropriation action
			self.act(new Appropriate(self.pool, appropriateAmount));
		}
	}

	@Override
	void reviseBehaviour() {

	}

	void appeal() {
	}
}
