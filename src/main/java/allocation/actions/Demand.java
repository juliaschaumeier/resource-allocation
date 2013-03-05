package allocation.actions;

import allocation.facts.Profile;

public class Demand {

	final String agent;
	final int round;
	final double quantity;
	final int pool;
	final Profile profile;
	
	public Demand( String agent,int round, double quantity, int pool, Profile profile) {
		super();
		this.agent = agent;
		this.round = round;
		this.quantity = quantity;
		this.pool = pool;
		this.profile = profile;
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

	public Profile getProfile() {
		return profile;
	}

	@Override
	public String toString() {
		return "Demand ["+ agent +", " + String.format("%.2f", quantity) + "]";
	};
}
