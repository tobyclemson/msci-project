package msci.mg.factories;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import msci.mg.Friendship;
import msci.mg.agents.Agent;
import org.apache.commons.collections15.Factory;

import java.util.Collection;

public class CompleteSocialNetworkFactory extends SocialNetworkFactory {
    public CompleteSocialNetworkFactory(
            Factory<Agent> agentFactory,
            Factory<Friendship> friendshipFactory,
            int numberOfAgents) {
        super(agentFactory, friendshipFactory, numberOfAgents);
    }

    @Override
    public Graph<Agent, Friendship> create() {
        Graph<Agent, Friendship> socialNetwork = new SparseGraph<Agent, Friendship>();

        int numberOfAgents = getNumberOfAgents();
        Factory<Agent> agentFactory = getAgentFactory();
        Factory<Friendship> friendshipFactory = getFriendshipFactory();

        for (int i = 0; i < numberOfAgents; i++) {
            socialNetwork.addVertex(agentFactory.create());
        }

        Collection<Agent> agents = socialNetwork.getVertices();

        for (Agent currentAgent : agents) {
            for (Agent otherAgent : agents) {
                if (currentAgent == otherAgent) {
                    continue;
                } else {
                    socialNetwork.addEdge(friendshipFactory.create(), currentAgent, otherAgent);
                }
            }
        }

        return socialNetwork;
    }
}
