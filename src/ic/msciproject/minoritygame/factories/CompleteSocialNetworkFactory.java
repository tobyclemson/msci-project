package ic.msciproject.minoritygame.factories;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import ic.msciproject.minoritygame.AbstractAgent;
import ic.msciproject.minoritygame.Friendship;
import java.util.Collection;
import org.apache.commons.collections15.Factory;

/**
 * The CompleteSocialNetworkFactory creates a social network of the specified
 * number of agents where each agent is connected to all other i.e., the graph
 * simple and complete and every possible pair of vertices is connected by an
 * edge.
 * @author tobyclemson
 */
public class CompleteSocialNetworkFactory extends SocialNetworkFactory {

    /**
     * Constructs an CompleteSocialNetworkFactory with the supplied agent and
     * friendship factories that will create social networks containing the
     * specified number of agents.
     * @param agentFactory A factory that generates agents to be used as nodes
     * in the social network.
     * @param friendshipFactory A factory that generates friendships to be used
     * as edges in the social network.
     * @param numberOfAgents An integer representing the number of agents that
     * should be present in the constructed social network.
     */
    public CompleteSocialNetworkFactory(
        Factory<AbstractAgent> agentFactory,
        Factory<Friendship> friendshipFactory,
        int numberOfAgents
    ) {
        super(agentFactory, friendshipFactory, numberOfAgents);
    }

    /**
     * Creates a social network containing the specified number of agents
     * each connected to all others.
     * @return An complete social network containing the specified number of
     * agents.
     */
    @Override
    public Graph<AbstractAgent, Friendship> create() {
        Graph<AbstractAgent, Friendship> socialNetwork =
            new SparseGraph<AbstractAgent, Friendship>();

        int numberOfAgents = getNumberOfAgents();
        Factory<AbstractAgent> agentFactory = getAgentFactory();
        Factory<Friendship> friendshipFactory = getFriendshipFactory();

        for(int i = 0; i < numberOfAgents; i++) {
            socialNetwork.addVertex(agentFactory.create());
        }

        Collection<AbstractAgent> agents = socialNetwork.getVertices();

        for(AbstractAgent currentAgent : agents) {
            for(AbstractAgent otherAgent : agents) {
                if(currentAgent == otherAgent) {
                    continue;
                } else {
                    socialNetwork.addEdge(
                        friendshipFactory.create(), currentAgent, otherAgent
                    );
                }
            }
        }
        
        return socialNetwork;
    }

}
