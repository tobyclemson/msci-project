package msci.mg.factories;

import cern.jet.random.AbstractDistribution;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import java.util.HashSet;
import java.util.Set;
import msci.mg.Friendship;
import msci.mg.agents.Agent;
import org.apache.commons.collections15.Factory;

/**
 * The {@code ScaleFreeSocialNetworkFactory} creates a social network such that
 * the underlying graph is a Barabasi-Albert scale-free graph.
 *
 * @author Toby Clemson
 */
public class ScaleFreeSocialNetworkFactory extends SocialNetworkFactory {
    private int averageNumberOfFriends;

    /**
     * Sets the agent and friendship factories to the supplied
     * {@code AgentFactory} and {@code FriendshipFactory} instances and sets the
     * number of agents and average number of friends attributes to the supplied
     * values.
     *
     * @param agentFactory A factory that creates agents for use as vertices in
     * the social network.
     * @param friendshipFactory A factory that creates friendships for use as
     * edges in the social network.
     * @param numberOfAgents The number of agents required in the social
     * network.
     * @param averageNumberOfFriends The average number of friends each agent
     * in the graph should have.
     */
    public ScaleFreeSocialNetworkFactory(
        AgentFactory agentFactory,
        FriendshipFactory friendshipFactory,
        int numberOfAgents,
        int averageNumberOfFriends
    ) {
        super(agentFactory, friendshipFactory, numberOfAgents);

        this.averageNumberOfFriends = averageNumberOfFriends;
    }

    public int getAverageNumberOfFriends() {
        return this.averageNumberOfFriends;
    }

    public void setAverageNumberOfFriends(int averageNumberOfFriends) {
        this.averageNumberOfFriends = averageNumberOfFriends;
    }

    @Override public Graph<Agent,Friendship> create() {
        int numberOfFriendshipsForEachNewAgent =
            ((getNumberOfAgents() * getAverageNumberOfFriends()) /
            (2 * (getNumberOfAgents() - getAverageNumberOfFriends())));
        int numberOfInitialAgents = numberOfFriendshipsForEachNewAgent;

        Set<Agent> initialAgents = new HashSet<Agent>();

        BarabasiAlbertGenerator<Agent,Friendship> graphGenerator =
            new BarabasiAlbertGenerator<Agent,Friendship>(
                getGraphFactory(),
                getAgentFactory(),
                getFriendshipFactory(),
                numberOfInitialAgents,
                numberOfFriendshipsForEachNewAgent,
                initialAgents
            );

        int numberOfAgentsToBeAdded =
            getNumberOfAgents() - numberOfInitialAgents;

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
