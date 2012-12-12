package allocation.agents;

abstract class AgentBehaviour implements Behaviour {

	final Agent self;

	AgentBehaviour(Agent self) {
		super();
		this.self = self;
	}

	abstract void appropriate();

	abstract void reviseBehaviour();

	static Behaviour getBehaviour(Agent a) {
		switch (a.role) {
		case MEMBER:
			return new MemberBehaviour(a);
		case NONMEMBER:
			return new NonMemberBehaviour(a);
		case HEAD:
			return new HeadBehaviour(a);
		default:
			return null;
		}
	}

}
