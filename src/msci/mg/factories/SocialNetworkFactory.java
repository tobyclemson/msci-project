package msci.mg.factories;

import edu.uci.ics.jung.graph.Graph;
import msci.mg.Friendship;
import msci.mg.agents.Agent;
import org.apache.commons.collections15.Factory;

public abstract class SocialNetworkFactory implements Factory<Graph<Agent, Friendship>> {
    private Factory<Agent> agentFactory;
    private Factory<Friendship> friendshipFactory;
    private int numberOfAgents;

    public SocialNetworkFactory(
            Factory<Agent> agentFactory,
            Factory<Friendship> friendshipFactory,
            int numberOfAgents) {
        this.agentFactory = agentFactory;
        this.friendshipFactory = friendshipFactory;
        this.numberOfAgents = numberOfAgents;
    }

    public Factory<Agent> getAgentFactory() {
        return this.agentFactory;
    }

    public Factory<Friendship> getFriendshipFactory() {
        return this.friendshipFactory;
    }

    public int getNumberOfAgents() {
        return this.numberOfAgents;
    }

    public void setAgentFactory(Factory<Agent> agentFactory) {
        this.agentFactory = agentFactory;
    }

    public void setFriendshipFactory(Factory<Friendship> friendshipFactory) {
        this.friendshipFactory = friendshipFactory;
    }

    public void setNumberOfAgents(int numberOfAgents) {
        this.numberOfAgents = numberOfAgents;
    }
}
