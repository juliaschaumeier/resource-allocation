package allocation.actions;

import allocation.agents.Agent;

public class Allocation extends PlayerAction {

	final public double quantity;

	public Allocation(double quantity) {
		super();
		this.quantity = quantity;
	}

	public Allocation(int t, Agent player, double quantity) {//not tied to pool or Institution??
		super(t, player);
		this.quantity = quantity;
	}

	public double getQuantity() {
		return quantity;
	}

	@Override
	public String toString() {
		return "Allocation [quantity=" + quantity + ", player=" + player
				+ ", round=" + round + "]";
	}

}
