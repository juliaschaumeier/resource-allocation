package allocation.actions;

import uk.ac.imperial.presage2.core.Action;

public abstract class TimestampedAction implements Action {

	int round;

	TimestampedAction() {
		super();
	}

	TimestampedAction(int t) {
		super();
		this.round = t;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int t) {
		this.round = t;
	}

}
