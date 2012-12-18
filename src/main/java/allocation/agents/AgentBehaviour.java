package allocation.agents;

import uk.ac.imperial.presage2.core.util.random.Random;
import allocation.actions.Appropriate;

abstract class AgentBehaviour implements Behaviour {

	final Agent self;

	AgentBehaviour(Agent self) {
		super();
		this.self = self;
	}

	abstract void appropriate();

	abstract void reviseBehaviour();

	static Behaviour getBehaviour(Agent a, Behaviour current) {
		switch (a.role) {
		case MEMBER:
			return current instanceof MemberBehaviour ? current
					: new MemberBehaviour(a);
		case NONMEMBER:
			return current instanceof NonMemberBehaviour ? current
					: new NonMemberBehaviour(a);
		case HEAD:
			return current instanceof HeadBehaviour ? current
					: new HeadBehaviour(a);
		default:
			return null;
		}
	}

}
