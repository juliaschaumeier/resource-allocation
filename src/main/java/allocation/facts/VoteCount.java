package allocation.facts;

import java.util.Map;

public class VoteCount {

	public final int institution;
	public final String ballot;
	public final int round;
	public final Map<Integer, Integer> result;

	public VoteCount(int institution, String ballot, int round, Map<Integer, Integer> result) {
		super();
		this.institution = institution;
		this.ballot = ballot;
		this.round = round;
		this.result = result;
	}

	@Override
	public String toString() {
		return "VoteCount [institution=" + institution + ", ballot=" + ballot
				+ ", round=" + round + ", result=" + result + "]";
	}

	public int getInstitution() {
		return institution;
	}

	public String getBallot() {
		return ballot;
	}

	public int getRound() {
		return round;
	}

	public Map<Integer, Integer> getResult() {
		return result;
	}

}
