//whole package not used anymore
package allocation.agents;

import uk.ac.imperial.presage2.core.util.random.Random;
import allocation.actions.Appropriate;

class NonMemberBehaviour extends AgentBehaviour {

	NonMemberBehaviour(Agent self) {
		super(self);
	}

	@Override
	public void doBehaviour() {
		// TODO Auto-generated method stub

	}

	@Override
	void appropriate() {// julia: refers to original compliancydegree!!
		double appropriateAmount = 0;
		if (self.compliancyDegree > 1 && self.active && Random.randomDouble() < outAppropriationFrequency) {
			appropriateAmount = self.standardRequest * self.compliancyDegree;
			//choose a random pool!! (so far we only created one??)
			self.act(new Appropriate(pool, appropriateAmount));
		}
	}

	@Override
	void reviseBehaviour() {
		// TODO Auto-generated method stub

	}

}
