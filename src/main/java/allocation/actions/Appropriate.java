package allocation.actions;

public class Appropriate extends PlayerAction {

	public final int pool;
	public final double amount;

	public Appropriate(int pool, double amount) {
		super();
		this.pool = pool;
		this.amount = amount;
	}

	public int getPool() {
		return pool;
	}

	public double getAmount() {
		return amount;
	}

	@Override
	public String toString() {
		return "Appropriate [amount=" + amount + ", player=" + player + ", t="
				+ round + "]";
	}

}
