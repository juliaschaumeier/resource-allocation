package allocation.actions;

public class CallForVote {

	final boolean head;
	final boolean raMethod;

	public CallForVote(boolean head, boolean raMethod) {
		super();
		this.head = head;
		this.raMethod = raMethod;
	}

	public boolean isHead() {
		return head;
	}

	public boolean isRaMethod() {
		return raMethod;
	}

	@Override
	public String toString() {
		return "CallForVote [head=" + head + ", raMethod=" + raMethod + "]";
	}

}
