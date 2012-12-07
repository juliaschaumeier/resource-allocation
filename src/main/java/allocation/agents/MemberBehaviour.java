package allocation.agents;

import allocation.actions.Appropriate;

class MemberBehaviour extends AgentBehaviour {

	MemberBehaviour(Agent self) {
		super(self);
	}

	@Override
	public void doBehaviour() {
		switch (self.poolService.getPoolState(self.pool)) {
		case Appropriate:
			appropriate();
			break;
		default:
			break;
		}
	}

	void vote() {
	}

	void request() {
	}

	@Override
	void appropriate() {
		if (self.active) {
			double appropriateAmount = 0;
			if (self.poolService.getIntitution(self.pool).isPrinciple2()) {
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
