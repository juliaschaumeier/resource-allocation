package allocation.facts;

public class Institution {

	final boolean principle1 = true;
	boolean principle2 = false;
	boolean principle3 = false;
	boolean principle4 = false;
	boolean principle5 = false;
	boolean principle6 = false;

	public Institution(boolean principle2, boolean principle3,
			boolean principle4, boolean principle5, boolean principle6) {
		super();
		this.principle2 = principle2;
		this.principle3 = principle3;
		this.principle4 = principle4;
		this.principle5 = principle5;
		this.principle6 = principle6;
	}

	public boolean isPrinciple1() {
		return principle1;
	}

	public boolean isPrinciple2() {
		return principle2;
	}

	public boolean isPrinciple3() {
		return principle3;
	}

	public boolean isPrinciple4() {
		return principle4;
	}

	public boolean isPrinciple5() {
		return principle5;
	}

	public boolean isPrinciple6() {
		return principle6;
	}

}
