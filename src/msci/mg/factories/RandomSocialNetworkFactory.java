package msci.mg.factories;

import edu.uci.ics.jung.algorithms.generators.GraphGenerator;
import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import msci.mg.AbstractAgent;
import msci.mg.Friendship;
import org.apache.commons.collections15.Factory;

/**
 * The RandomSocialNetworkFactory class creates a social network of agents and
 * their friendships where the network is random as defined by Erdos and Renyi.
 * Every pair of vertices is connected with a link probability, p and as such
 * the average degree of any vertex in the graph is pN, where N is the number
 * of vertices in the graph.
 * @author tobyclemson
 */
public class RandomSocialNetworkFactory extends SocialNetworkFactory {

    /**
     * A double representing the probability that two agents are connected
     * when constructing the social network.
     */
    private double linkProbability;

    /**
     * Constructs a RandomSocialNetworkFactory with the supplied agent and
     * friendship factories that will create social networks containing the
     * specified number of agents each friends with one another with probability
     * as specified.
     * @param agentFactory A factory that generates agents to be used as nodes
     * in the social network.
     * @param friendshipFactory A factory that generates friendships to be used
     * as edges in the social network.
     * @param numberOfAgents An integer representing the number of agents that
     * should be present in the constructed social network.
     * @param linkProbability A double representing the probability that two
     * agents are connected by an edge. This value must be greater than or equal
     * to 0.0 and less than or equal to 1.0 otherwise an
     * IllegalArgumentException will be raised.
     */
    public RandomSocialNetworkFactory(
        Factory<AbstractAgent> agentFactory,
        Factory<Friendship> friendshipFactory,
        int numberOfAgents,
        double linkProbability
    ) {
        // call super's constructor
        super(agentFactory, friendshipFactory, numberOfAgents);

        // throw an exception unless the supplied link probability is valid
        assertProbability(linkProbability);

        // set the link probability to the supplied value
        this.linkProbability = linkProbability;
    }

    /**
     * Returns the link probability associated with this factory which
     * represents the probability that any two vertices in the graph created are
     * connected by a friendship.
     * @return The associated link probability.
     */
    public double getLinkProbability() {
        return this.linkProbability;
    }

    /**
     * 
     * @param linkProbability
     */
    public void setLinkProbability(double linkProbability) {
        // throw an exception unless the supplied link probability is valid
        assertProbability(linkProbability);

        // set the link probability to the supplied value
        this.linkProbability = linkProbability;
    }

    /**
     * 
     * @return
     */
    @Override
    public Graph<AbstractAgent, Friendship> create() {
        // create a graph factory
        Factory<UndirectedGraph<AbstractAgent, Friendship>> graphFactory =
            new Factory<UndirectedGraph<AbstractAgent, Friendship>>()
        {
            public UndirectedGraph<AbstractAgent, Friendship> create() {
                return new UndirectedSparseGraph<AbstractAgent, Friendship>();
            }
        };

        // fetch the agent and friendship factories
        Factory<AbstractAgent> requiredAgentFactory = getAgentFactory();
        Factory<Friendship> requiredFriendshipFactory = getFriendshipFactory();

        // fetch the required number of agents and link probability
        int requiredNumberOfAgents = getNumberOfAgents();
        double requiredLinkProbability = getLinkProbability();

        // construct an Erdos-Reyni graph generator
        GraphGenerator<AbstractAgent, Friendship> erdosReyniGraphGenerator =
            new ErdosRenyiGenerator<AbstractAgent, Friendship>(
                graphFactory,
                requiredAgentFactory,
                requiredFriendshipFactory,
                requiredNumberOfAgents,
                requiredLinkProbability
            );

        // construct and return a random graph
        return erdosReyniGraphGenerator.create();
    }

    /**
     *
     * @param probability
     */
    private void assertProbability(double probability) {
        if(probability < 0.0 || probability > 1.0) {
            throw new IllegalArgumentException(
                "Probabilities must lie in the interval (0.0, 1.0)."
            );
        }
    }

}
