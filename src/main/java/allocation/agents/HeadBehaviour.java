package allocation.agents;

import allocation.actions.CallForVote;

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
		case Vote:
			vote();
			break;
		case Appropriate:
			appropriate();
			break;
		default:
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

	void allocate() {
	}

	void sanction() {
	}

	void uphold() {
	}

}
