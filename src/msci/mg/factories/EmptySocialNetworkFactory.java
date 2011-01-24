package msci.mg.factories;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import msci.mg.Friendship;
import msci.mg.agents.Agent;
import org.apache.commons.collections15.Factory;

public class EmptySocialNetworkFactory extends SocialNetworkFactory {
    public EmptySocialNetworkFactory(
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

        for (int i = 0; i < numberOfAgents; i++) {
            socialNetwork.addVertex(agentFactory.create());
        }

        return socialNetwork;
    }

}
