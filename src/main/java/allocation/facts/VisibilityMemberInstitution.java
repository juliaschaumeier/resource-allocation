//not used anymore
package allocation.facts;

import allocation.Phase;

public interface VisibilityMemberInstitution {

	public boolean isPrinciple1();

	public boolean isPrinciple2();

	public boolean isPrinciple3();

	public boolean isPrinciple4();

	public boolean isPrinciple5();

	public boolean isPrinciple6();

	public Phase getState();

	public int getRound();

	public int getInitialAgents();

	public boolean isVoteHead();

	public boolean isVoteRaMethod();

	public RaMethod getAllocationMethod();

}
