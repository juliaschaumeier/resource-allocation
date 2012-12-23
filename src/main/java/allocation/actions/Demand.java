package allocation.actions;

public class Demand {

	final int round;
	final int pool;
	final String agent;
	final double quantity;

	public Demand(int round, int pool, String agent, double quantity) {
		super();
		this.round = round;
		this.pool = pool;
		this.agent = agent;
		this.quantity = quantity;
	}

	public double getQuantity() {
		return quantity;
	}

	public int getPool() {
		return pool;
	}

	public int getRound() {
		return round;
	}

	public String getAgent() {
		return agent;
	}

	@Override
	public String toString() {
		return "Demand [quantity=" + quantity + "]";
	};
}
