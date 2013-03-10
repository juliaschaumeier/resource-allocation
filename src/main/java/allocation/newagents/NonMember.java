package allocation.newagents;

import uk.ac.imperial.presage2.core.util.random.Random;
//import allocation.actions.Allocation;
import allocation.facts.CommonPool;
//import allocation.facts.Institution;
import allocation.facts.RaMethod;
import allocation.facts.Profile;

public class NonMember extends Agent {


	public NonMember(String name, int id, double compliancyDegree, double initialCompliancyDegree,
			double standardRequest, int pool, Profile profile, RaMethod justicePrAbundance,
			RaMethod justicePrCrisis, int judgeSize, int judgeTolerance) {
		super(name, id, pool, compliancyDegree, initialCompliancyDegree, standardRequest,
				profile, justicePrAbundance, justicePrCrisis, judgeSize, judgeTolerance);
	}

	public NonMember(Member m) {
		super(m);
		// TODO 
	}

	public double appropriate(CommonPool pool) {
		double appropriateAmount = 0;
		if (initialCompliancyDegree > 1 && active
				&& Random.randomDouble() < pool.getOutAppropriationFrequency()) {
			appropriateAmount = standardRequest * initialCompliancyDegree;
		}
		if (appropriateAmount < 0){
			appropriateAmount = 0;
		}
		return appropriateAmount;
	}

}
