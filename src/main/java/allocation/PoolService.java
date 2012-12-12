package allocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.drools.runtime.ObjectFilter;
import org.drools.runtime.StatefulKnowledgeSession;

import uk.ac.imperial.presage2.core.environment.EnvironmentService;
import uk.ac.imperial.presage2.core.environment.EnvironmentSharedStateAccess;
import allocation.facts.CommonPool;
import allocation.facts.Institution;
import allocation.facts.ResourceMonitor;

import com.google.inject.Inject;

public class PoolService extends EnvironmentService {

	final StatefulKnowledgeSession session;
	Map<Integer, CommonPool> pools = new HashMap<Integer, CommonPool>();
	Map<Integer, Institution> institutions = new HashMap<Integer, Institution>();

	@Inject
	public PoolService(EnvironmentSharedStateAccess sharedState,
			StatefulKnowledgeSession session) {
		super(sharedState);
		this.session = session;
	}

	private void loadPools() {
		if (pools.isEmpty()) {
			Collection<Object> ps = session.getObjects(new ObjectFilter() {

				@Override
				public boolean accept(Object object) {
					return object instanceof CommonPool;
				}
			});
			for (Object o : ps) {
				if (o instanceof CommonPool) {
					CommonPool p = (CommonPool) o;
					pools.put(p.getId(), p);
				}
			}
		}
	}

	private void loadInstitutions() {
		if (institutions.isEmpty()) {
			Collection<Object> is = session.getObjects(new ObjectFilter() {

				@Override
				public boolean accept(Object object) {
					return object instanceof Institution;
				}
			});
			for (Object o : is) {
				if (o instanceof Institution) {
					Institution p = (Institution) o;
					institutions.put(p.getId(), p);
				}
			}
		}
	}

	public ResourceMonitor getPool(int poolId) {
		loadPools();
		if (pools.containsKey(poolId))
			return pools.get(poolId);
		else
			return null;
	}

	public Institution getInstitution(int id) {
		loadInstitutions();
		if (institutions.containsKey(id))
			return institutions.get(id);
		else
			return null;
	}

}
