package allocation.newagents;

import uk.ac.imperial.presage2.core.util.random.Random;
//import allocation.actions.Allocation;
import allocation.facts.CommonPool;
//import allocation.facts.Institution;

public class NonMember extends Agent {


	public NonMember(String name, double compliancyDegree, double initialCompliancyDegree,
			double standardRequest, int pool) {
		super(name, pool, compliancyDegree, initialCompliancyDegree, standardRequest);//what does pool mean here?? He doesn't have a pool really..
	}

	public NonMember(Member m) {//is that when a member becomes a non-member??
		super(m);
		// TODO 
	}

	public double appropriate(CommonPool pool) {
		double appropriateAmount = 0;
		if (initialCompliancyDegree > 1 && active
				&& Random.randomDouble() < pool.getOutAppropriationFrequency()) {
			appropriateAmount = standardRequest * initialCompliancyDegree;
		}
		return appropriateAmount;
	}

}
