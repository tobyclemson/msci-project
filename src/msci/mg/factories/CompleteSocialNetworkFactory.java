package msci.mg.factories;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import msci.mg.Friendship;
import java.util.Collection;
import msci.mg.agents.Agent;
import org.apache.commons.collections15.Factory;

/**
 * The {@code CompleteSocialNetworkFactory} creates a social network of the
 * specified number of agents where each agent is connected to all others i.e.,
 * the graph is simple and complete and every possible pair of vertices is
 * connected by an edge.
 *
 * @author Toby Clemson
 */
public class CompleteSocialNetworkFactory extends SocialNetworkFactory {

    /**
     * Sets the agent and friendship factories to the supplied factories and
     * sets the number of agents attribute to the supplied value.
     *
     * @param agentFactory A factory that generates agents to be used as 
     * vertices in the social network.
     * @param friendshipFactory A factory that generates friendships to be used
     * as edges in the social network.
     * @param numberOfAgents The number of agents that should be present in the
     * constructed social network.
     */
    public CompleteSocialNetworkFactory(
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
        Factory<Friendship> friendshipFactory = getFriendshipFactory();

        for(int i = 0; i < numberOfAgents; i++) {
            socialNetwork.addVertex(agentFactory.create());
        }

        Collection<Agent> agents = socialNetwork.getVertices();

        for(Agent currentAgent : agents) {
            for(Agent otherAgent : agents) {
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
