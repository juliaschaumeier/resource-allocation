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
	
	void appropriate() {//julia: replace with abstract void? will it be overwritten by the agent's appropriate function?
		if (self.active) {
			double appropriateAmount = 0;
				if (self.compliancyDegree > 1 && Random.randomDouble() < outapprperc) {
					//agent doesn't place request; for outmonitor: outapprperc -= outapprperc*scarefactor; 0.1*0.1 typically.
					appropriate = standardreq*originalgreedity;// do something...
				}
			// do appropriation action
			self.act(new Appropriate(self.pool, appropriateAmount));
		}
	}

}
