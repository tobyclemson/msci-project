package msci.mg.factories;

import edu.uci.ics.jung.algorithms.generators.GraphGenerator;
import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import msci.mg.Friendship;
import msci.mg.agents.Agent;
import org.apache.commons.collections15.Factory;

/**
 * The {@code RandomSocialNetworkFactory} class creates a social network where
 * all agents are connected to each other with some probability, i.e., the
 * resulting graph is random as defined by Erdos and Renyi.
 *
 * Every pair of vertices is connected with a link probability, p and as such
 * the average degree of any vertex in the graph is pN, where N is the number
 * of vertices in the graph.
 *
 * @author Toby Clemson
 */
public class RandomSocialNetworkFactory extends SocialNetworkFactory {

    private double linkProbability;

    /**
     * Sets the agent and friendship factories to the supplied factories and
     * sets the number of agents and link probability attributes to the supplied
     * values.
     *
     * @param agentFactory A factory that generates agents to be used as
     * vertices in the social network.
     * @param friendshipFactory A factory that generates friendships to be used
     * as edges in the social network.
     * @param numberOfAgents The number of agents that should be present in the
     * constructed social network.
     * @param linkProbability A double representing the probability that two
     * agents are connected by an edge. This value must be greater than or equal
     * to 0.0 and less than or equal to 1.0 otherwise an
     * {@code IllegalArgumentException} will be raised.
     */
    public RandomSocialNetworkFactory(
        Factory<Agent> agentFactory,
        Factory<Friendship> friendshipFactory,
        int numberOfAgents,
        double linkProbability
    ) {
        super(agentFactory, friendshipFactory, numberOfAgents);
        setLinkProbability(linkProbability);
    }

    public double getLinkProbability() {
        return this.linkProbability;
    }

    /**
     * If the value supplied for the link probability is not in the open
     * interval [0.0, 1.0] then an {@code IllegalArgumentException} is thrown.
     */
    public void setLinkProbability(double linkProbability) {
        assertProbability(linkProbability);
        this.linkProbability = linkProbability;
    }

    @Override
    public Graph<Agent, Friendship> create() {
        return getSocialNetworkGenerator().create();
    }

    private GraphGenerator<Agent,Friendship> getSocialNetworkGenerator() {
        return new ErdosRenyiGenerator<Agent, Friendship>(
            getGraphFactory(),
            getAgentFactory(),
            getFriendshipFactory(),
            getNumberOfAgents(),
            getLinkProbability()
        );
    }

    private Factory<UndirectedGraph<Agent, Friendship>> getGraphFactory() {
        return new Factory<UndirectedGraph<Agent, Friendship>>() {
            public UndirectedGraph<Agent, Friendship> create() {
                return new UndirectedSparseGraph<Agent, Friendship>();
            }
        };
    }

    private void assertProbability(double probability) {
        if(probability < 0.0 || probability > 1.0) {
            throw new IllegalArgumentException(
                "Probabilities must lie in the interval (0.0, 1.0)."
            );
        }
    }

}
