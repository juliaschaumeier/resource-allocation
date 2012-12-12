package allocation.actions;

public class Demand extends PlayerAction {

	final double quantity;

	public Demand(double quantity) {
		super();
		this.quantity = quantity;
	}

	public double getQuantity() {
		return quantity;
	}

	@Override
	public String toString() {
		return "Demand [quantity=" + quantity + "]";
	};
}
