package allocation.facts;

import java.util.Queue;

import allocation.actions.Demand;

public interface VisibilityHeadInstitution extends VisibilityMemberInstitution {

	public void setAllocationMethod(RaMethod allocationMethod);

	public void setVoteRaMethod(boolean voteRaMethod);

	public void setVoteHead(boolean voteHead);

	public Queue<Demand> getDemandQueue();

}
