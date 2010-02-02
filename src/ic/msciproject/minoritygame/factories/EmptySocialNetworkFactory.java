package ic.msciproject.minoritygame.factories;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import ic.msciproject.minoritygame.AbstractAgent;
import ic.msciproject.minoritygame.Friendship;
import org.apache.commons.collections15.Factory;

/**
 * The EmptySocialNetworkFactory creates a social network of the specified
 * number of agents but with no friendships present i.e., the graph is
 * completely disconnected and no two vertices are connected by an edge.
 * @author tobyclemson
 */
public class EmptySocialNetworkFactory extends SocialNetworkFactory{

    /**
     * Constructs an EmptySocialNetworkFactory with the supplied agent and
     * friendship factories that will create social networks containing the
     * specified number of agents.
     * @param agentFactory A factory that generates agents to be used as nodes
     * in the social network.
     * @param friendshipFactory A factory that generates friendships to be used
     * as edges in the social network.
     * @param numberOfAgents An integer representing the number of agents that
     * should be present in the constructed social network.
     */
    public EmptySocialNetworkFactory(
        Factory<AbstractAgent> agentFactory,
        Factory<Friendship> friendshipFactory,
        int numberOfAgents
    ) {
        super(agentFactory, friendshipFactory, numberOfAgents);
    }

    /**
     * Creates a social network containing the specified number of agents
     * but no friendships.
     * @return An empty social network containing the specified number of 
     * agents.
     */
    @Override
    public Graph<AbstractAgent, Friendship> create() {
        Graph<AbstractAgent, Friendship> socialNetwork =
            new SparseGraph<AbstractAgent, Friendship>();

        int numberOfAgents = getNumberOfAgents();
        Factory<AbstractAgent> agentFactory = getAgentFactory();

        for(int i = 0; i < numberOfAgents; i++) {
            socialNetwork.addVertex(agentFactory.create());
        }

        return socialNetwork;
    }

}
