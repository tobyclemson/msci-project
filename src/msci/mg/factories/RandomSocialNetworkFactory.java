package msci.mg.factories;

import edu.uci.ics.jung.algorithms.generators.GraphGenerator;
import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import msci.mg.Friendship;
import msci.mg.agents.Agent;
import org.apache.commons.collections15.Factory;

public class RandomSocialNetworkFactory extends SocialNetworkFactory {
    private double linkProbability;

    public RandomSocialNetworkFactory(
            Factory<Agent> agentFactory,
            Factory<Friendship> friendshipFactory,
            int numberOfAgents,
            double linkProbability) {
        super(agentFactory, friendshipFactory, numberOfAgents);
        setLinkProbability(linkProbability);
    }

    public double getLinkProbability() {
        return this.linkProbability;
    }

    public void setLinkProbability(double linkProbability) {
        assertProbability(linkProbability);
        this.linkProbability = linkProbability;
    }

    @Override
    public Graph<Agent, Friendship> create() {
        return getSocialNetworkGenerator().create();
    }

    private GraphGenerator<Agent, Friendship> getSocialNetworkGenerator() {
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
        if (probability < 0.0 || probability > 1.0) {
            throw new IllegalArgumentException("Probabilities must lie in the interval (0.0, 1.0).");
        }
    }

}
