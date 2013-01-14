//not used anymore
package allocation.facts;

import java.util.Queue;
import java.util.Set;

import allocation.actions.Demand;
import allocation.agents.Agent;

public interface VisibilityHeadInstitution extends VisibilityMemberInstitution {

	public void setAllocationMethod(RaMethod allocationMethod);

	public void setVoteRaMethod(boolean voteRaMethod);

	public void setVoteHead(boolean voteHead);

	public Queue<Demand> getDemandQueue();

	public Set<Agent> getMembers();

}
