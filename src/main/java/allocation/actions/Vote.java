package allocation.actions;

import allocation.facts.RaMethod;

public class Vote extends PlayerAction {

	final String ballot;
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

	@Override
	public String toString() {
		return "Vote [ballot=" + ballot + ", value=" + value + ", player="
				+ player + ", round=" + round + "]";
	}

}
