package allocation.newagents;

import uk.ac.imperial.presage2.core.util.random.Random;
import allocation.actions.Appropriate;
import allocation.facts.CommonPool;
import allocation.facts.Institution;

public class NonMember extends Agent {

	double appropriationFrequency;

	public NonMember(String name, double compliancyDegree,
			double standardRequest, int pool, double appropriationFrequency) {
		super(name, pool, compliancyDegree, standardRequest);
		this.appropriationFrequency = appropriationFrequency;
	}

	public NonMember(Member m) {
		super(m);
		// TODO how to set appropriationFrequency?
		appropriationFrequency = 0.5;
	}

	public double appropriate(CommonPool pool) {
		double appropriateAmount = 0;
		if (compliancyDegree > 1 && active
				&& Random.randomDouble() < appropriationFrequency) {
			appropriateAmount = standardRequest * compliancyDegree;
			// choose a random pool!! (so far we only created one??)
			return appropriateAmount;
		} else {
			return 0;
		}
	}

	public double getAppropriationFrequency() {
		return appropriationFrequency;
	}

}
