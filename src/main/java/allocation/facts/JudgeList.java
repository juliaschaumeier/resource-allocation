package allocation.facts;

import java.util.List;

public class JudgeList {
	
	public final String agent;
	public final int round;
	public final List<String> list;
	public final int institution;
	
	public JudgeList(String agent, int round, List<String> list, int institution){
		super();
		this.agent = agent;
		this.round = round;
		this.list = list;
		this.institution = institution;
	}

	@Override
	public String toString() {
		return "JudgeList [agent=" + agent + ", round=" + round + ", " +
				"list=" + list + ", institution=" + institution + "]";
	}

	public String getAgent() {
		return agent;
	}

	public int getRound() {
		return round;
	}

	public List<String> getList() {
		return list;
	}

	public int getInstitution() {
		return institution;
	}

}
