package allocation.actions;

import allocation.facts.RaMethod;
import allocation.newagents.Member;

public class Vote {

	String voter;
	final String ballot;
	int round;
	final Integer value;

	public Vote(String ballot, Integer value) {
		super();
		this.ballot = ballot;
		this.value = value;
	}

	public static Vote voteRaMethod(RaMethod value) {
		return new Vote("raMethod", value.ordinal());
	}
	public static Vote voteHead(Member value){ 
		if(value != null){
			return new Vote("head", value.getId());
		} else {
			return new Vote("head", (Integer) null);
		}
		
	}

	public String getBallot() {
		return ballot;
	}

	public Integer getValue() {
		return value;
	}

	public String getVoter() {
		return voter;
	}

	public void setVoter(String voter) {
		this.voter = voter;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	@Override
	public String toString() {
		return "Vote [ballot=" + ballot + ", value=" + value + ", voter="
				+ voter + ", round=" + round + "]";
	}

}
