package msci.mg.factories;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import msci.mg.Friendship;
import msci.mg.agents.Agent;
import org.apache.commons.collections15.Factory;

import java.util.ArrayList;
import java.util.List;

public class RegularRingSocialNetworkFactory extends SocialNetworkFactory {
    private int numberOfFriendsInEachDirection;

    public RegularRingSocialNetworkFactory(
            AgentFactory agentFactory,
            FriendshipFactory friendshipFactory,
            int numberOfAgents,
            int numberOfFriendsInEachDirection
    ) {
        super(agentFactory, friendshipFactory, numberOfAgents);

        this.numberOfFriendsInEachDirection = numberOfFriendsInEachDirection;
    }

    public int getNumberOfFriendsInEachDirection() {
        return this.numberOfFriendsInEachDirection;
    }

    public void setNumberOfFriendsInEachDirection(int numberOfFriendsInEachDirection) {
        this.numberOfFriendsInEachDirection = numberOfFriendsInEachDirection;
    }

    public Graph<Agent, Friendship> create() {
        Graph<Agent, Friendship> socialNetwork = new SparseGraph<Agent, Friendship>();

        int requiredNumberOfAgents = getNumberOfAgents();
        int requiredNumberOfFriendsInEachDirection = getNumberOfFriendsInEachDirection();
        Factory<Agent> suppliedAgentFactory = getAgentFactory();
        Factory<Friendship> suppliedFriendshipFactory = getFriendshipFactory();

        for (int v = 0; v < requiredNumberOfAgents; v++) {
            socialNetwork.addVertex(suppliedAgentFactory.create());
        }

        List<Agent> agents = new ArrayList<Agent>(socialNetwork.getVertices());

        for (int a = 0; a < agents.size(); a++) {
            Agent currentAgent = agents.get(a);

            for (int f = 1; f <= requiredNumberOfFriendsInEachDirection; f++) {
                int boundary = agents.size();

                socialNetwork.addEdge(
                        suppliedFriendshipFactory.create(),
                        currentAgent,
                        agents.get((boundary + (a + f)) % boundary));
                socialNetwork.addEdge(
                        suppliedFriendshipFactory.create(),
                        currentAgent,
                        agents.get((boundary + (a - f)) % boundary));
            }
        }

        return socialNetwork;
    }
}
