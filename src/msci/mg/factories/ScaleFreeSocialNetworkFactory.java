package msci.mg.factories;

import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import msci.mg.Friendship;
import msci.mg.agents.Agent;
import org.apache.commons.collections15.Factory;

import java.util.HashSet;
import java.util.Set;

public class ScaleFreeSocialNetworkFactory extends SocialNetworkFactory {
    private int averageNumberOfFriends;

    public ScaleFreeSocialNetworkFactory(
            AgentFactory agentFactory,
            FriendshipFactory friendshipFactory,
            int numberOfAgents,
            int averageNumberOfFriends) {
        super(agentFactory, friendshipFactory, numberOfAgents);
        this.averageNumberOfFriends = averageNumberOfFriends;
    }

    public int getAverageNumberOfFriends() {
        return this.averageNumberOfFriends;
    }

    public void setAverageNumberOfFriends(int averageNumberOfFriends) {
        this.averageNumberOfFriends = averageNumberOfFriends;
    }

    @Override
    public Graph<Agent, Friendship> create() {
        int numberOfFriendshipsForEachNewAgent =
                ((getNumberOfAgents() * getAverageNumberOfFriends()) /
                        (2 * (getNumberOfAgents() - getAverageNumberOfFriends())));
        int numberOfInitialAgents = numberOfFriendshipsForEachNewAgent;

        Set<Agent> initialAgents = new HashSet<Agent>();

        BarabasiAlbertGenerator<Agent, Friendship> graphGenerator =
                new BarabasiAlbertGenerator<Agent, Friendship>(
                        getGraphFactory(),
                        getAgentFactory(),
                        getFriendshipFactory(),
                        numberOfInitialAgents,
                        numberOfFriendshipsForEachNewAgent,
                        initialAgents);

        int numberOfAgentsToBeAdded = getNumberOfAgents() - numberOfInitialAgents;

        graphGenerator.evolveGraph(numberOfAgentsToBeAdded);

        return graphGenerator.create();
    }

    private Factory<Graph<Agent, Friendship>> getGraphFactory() {
        return new Factory<Graph<Agent, Friendship>>() {
            public Graph<Agent, Friendship> create() {
                return new SparseGraph<Agent, Friendship>();
            }
        };
    }
}
