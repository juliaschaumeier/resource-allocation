package allocation.actions;

public class Demand extends PlayerAction {

	final int pool;
	final double quantity;

	public Demand(int pool, double quantity) {
		super();
		this.pool = pool;
		this.quantity = quantity;
	}

	public double getQuantity() {
		return quantity;
	}

	public int getPool() {
		return pool;
	}

	@Override
	public String toString() {
		return "Demand [quantity=" + quantity + "]";
	};
}
