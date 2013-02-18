package allocation.actions;

import allocation.facts.CommonPool;

public class Allocation {

	final String agent;
	final int round;
	final public double quantity;
	final CommonPool pool;

	public Allocation(String agent, int t, double quantity, CommonPool pool) {
		super();
		this.agent = agent;
		this.round = t;
		this.quantity = quantity;
		this.pool = pool;
	}
	
	public CommonPool getPool() {
		return pool;
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
		return "Allocation [" + agent +", " + String.format("%.2f", quantity) + ", rd=" + round + "]";
	}

}
