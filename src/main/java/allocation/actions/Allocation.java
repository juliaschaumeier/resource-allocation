package allocation.actions;

public class Allocation {

	final int round;
	final String agent;
	final public double quantity;

	public Allocation(int t, String agent, double quantity) {
		super();
		this.round = t;
		this.agent = agent;
		this.quantity = quantity;
	}

	public double getQuantity() {
		return quantity;
	}

	public int getRound() {
		return round;
	}

	public String getAgent() {
		return agent;
	}

	@Override
	public String toString() {
		return "Allocation [quantity=" + quantity + ", agent=" + agent
				+ ", round=" + round + "]";
	}

}
