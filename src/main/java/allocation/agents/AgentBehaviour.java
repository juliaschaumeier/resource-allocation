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
		case member:
			return new MemberBehaviour(a);
		case nonMember:
			return new NonMemberBehaviour(a);
		case head:
			return new HeadBehaviour(a);
		default:
			return null;
		}
	}

}
