package allocation.actions;

import allocation.facts.RaMethod;

public class Vote {

	String voter;
	final String ballot;
	int round;
	final int value;

	public Vote(String ballot, int value) {
		super();
		this.ballot = ballot;
		this.value = value;
	}

	public static Vote voteRaMethod(RaMethod value) {
		return new Vote("raMethod", value.ordinal());
	}

	public String getBallot() {
		return ballot;
	}

	public int getValue() {
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
