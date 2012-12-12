package allocation.actions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.drools.runtime.ObjectFilter;
import org.drools.runtime.StatefulKnowledgeSession;

import uk.ac.imperial.presage2.core.Action;
import uk.ac.imperial.presage2.core.environment.ActionHandler;
import uk.ac.imperial.presage2.core.environment.ActionHandlingException;
import uk.ac.imperial.presage2.core.environment.EnvironmentServiceProvider;
import uk.ac.imperial.presage2.core.environment.UnavailableServiceException;
import uk.ac.imperial.presage2.core.messaging.Input;
import allocation.PoolService;
import allocation.agents.Agent;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Takes agent actions and adds relevant metadata to them before inserting into
 * the drools session.
 * 
 */
@Singleton
public class AgentActionHandler implements ActionHandler {

	final private Logger logger = Logger.getLogger(AgentActionHandler.class);
	final StatefulKnowledgeSession session;
	Map<UUID, Agent> players = new HashMap<UUID, Agent>();
	final EnvironmentServiceProvider serviceProvider;
	PoolService pool = null;

	@Inject
	public AgentActionHandler(StatefulKnowledgeSession session,
			EnvironmentServiceProvider serviceProvider)
			throws UnavailableServiceException {
		super();
		this.session = session;
		this.serviceProvider = serviceProvider;
	}

	@Override
	public boolean canHandle(Action action) {
		return action instanceof PlayerAction;
	}

	PoolService getPoolService() {
		if (pool == null) {
			try {
				pool = this.serviceProvider
						.getEnvironmentService(PoolService.class);
			} catch (UnavailableServiceException e) {
				logger.warn("Couldn't get pool service", e);
			}
		}
		return pool;
	}

	private synchronized Agent getPlayer(final UUID id) {
		if (!players.containsKey(id)) {
			Collection<Object> rawPlayers = session
					.getObjects(new ObjectFilter() {
						@Override
						public boolean accept(Object object) {
							return object instanceof Agent;
						}
					});
			for (Object pObj : rawPlayers) {
				Agent p = (Agent) pObj;
				players.put(p.getID(), p);
			}
		}
		return players.get(id);
	}

	@Override
	public synchronized Input handle(Action action, UUID actor)
			throws ActionHandlingException {
		Agent p = getPlayer(actor);
		if (action instanceof PlayerAction) {
			((PlayerAction) action).setPlayer(p);
		}
		if (action instanceof TimestampedAction) {
			((TimestampedAction) action).setRound(getPoolService()
					.getInstitution(0).getRound());
		}
		session.insert(action);
		logger.debug("Handling: " + action);
		return null;
	}
}
