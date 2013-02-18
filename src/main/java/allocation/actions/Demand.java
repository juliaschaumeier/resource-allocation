package allocation.actions;

public class Demand {

	final String agent;
	final int round;
	final double quantity;
	final int pool;
	
	public Demand( String agent,int round, double quantity, int pool) {
		super();
		this.agent = agent;
		this.round = round;
		this.quantity = quantity;
		this.pool = pool;
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
		return "Demand ["+ agent +", " + String.format("%.2f", quantity) + "]";
	};
}
