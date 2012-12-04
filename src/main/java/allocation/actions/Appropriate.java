package allocation.actions;

public class Appropriate extends PlayerAction {

	public final double amount;

	public Appropriate(double amount) {
		super();
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Appropriate [amount=" + amount + ", player=" + player + ", t="
				+ t + "]";
	}

}
