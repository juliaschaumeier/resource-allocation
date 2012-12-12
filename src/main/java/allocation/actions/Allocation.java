package allocation.actions;

import allocation.agents.Agent;

public class Allocation extends PlayerAction {

	final double quantity;

	public Allocation(double quantity) {
		super();
		this.quantity = quantity;
	}

	public Allocation(int t, Agent player, double quantity) {
		super(t, player);
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Allocation [quantity=" + quantity + ", player=" + player
				+ ", round=" + round + "]";
	}

}
