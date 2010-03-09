package msci.mg.factories;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import msci.mg.Friendship;
import msci.mg.agents.Agent;
import org.apache.commons.collections15.Factory;

/**
 * The {@code EmptySocialNetworkFactory} creates a social network where none of
 * agents are connected to each other i.e., the graph is completely disconnected
 * and no two vertices are connected by an edge.
 *
 * @author Toby Clemson
 */
public class EmptySocialNetworkFactory extends SocialNetworkFactory{

    /**
     * Sets the agent and friendship factories to the supplied factories and the
     * number of agents attribute to the supplied value.
     *
     * @param agentFactory A factory that generates agents to be used as 
     * vertices in the social network.
     * @param friendshipFactory A factory that generates friendships to be used
     * as edges in the social network.
     * @param numberOfAgents An integer representing the number of agents that
     * should be present in the constructed social network.
     */
    public EmptySocialNetworkFactory(
        Factory<Agent> agentFactory,
        Factory<Friendship> friendshipFactory,
        int numberOfAgents
    ) {
        super(agentFactory, friendshipFactory, numberOfAgents);
    }

    @Override
    public Graph<Agent, Friendship> create() {
        Graph<Agent, Friendship> socialNetwork =
            new SparseGraph<Agent, Friendship>();

        int numberOfAgents = getNumberOfAgents();
        Factory<Agent> agentFactory = getAgentFactory();

        for(int i = 0; i < numberOfAgents; i++) {
            socialNetwork.addVertex(agentFactory.create());
        }

        return socialNetwork;
    }

}
