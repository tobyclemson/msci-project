package msci.mg.factories;

import edu.uci.ics.jung.graph.Graph;
import msci.mg.Friendship;
import msci.mg.agents.Agent;
import org.apache.commons.collections15.Factory;

/**
 * The {@code SocialNetworkFactory} is an abstract factory providing accessors
 * and mutators for common attributes required to construct a social network,
 * such as an agent factory, a friendship factory and the number of agents that
 * should be present in the resulting social network.
 *
 * @author Toby Clemson
 */
public abstract class SocialNetworkFactory
    implements Factory<Graph<Agent, Friendship>> {

    /**
     * The {@code Agent} instances created by this agent factory are used as
     * vertices in the social network constructed by this factory.
     */
    private Factory<Agent> agentFactory;

    /**
     * The {@code Friendship} instances created by this friendship factory are
     * used as the edges in the social network constructed by this factory.
     */
    private Factory<Friendship> friendshipFactory;

    private int numberOfAgents;

    /**
     * Sets the agent and friendship factories to the supplied factories and
     * sets the number of agents attribute to the supplied value.
     *
     * @param agentFactory A factory that creates agents for use as vertices in
     * the social network.
     * @param friendshipFactory A factory that creates friendships for use as
     * edges in the social network.
     * @param numberOfAgents The number of agents required in the social
     * network.
     */
    public SocialNetworkFactory(
        Factory<Agent> agentFactory,
        Factory<Friendship> friendshipFactory,
        int numberOfAgents
    ) {
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

    /**
     * {@code Agent} instances created by the supplied factory will be used as
     * vertices in the social network constructed by this factory.
     */
    public void setAgentFactory(Factory<Agent> agentFactory) {
        this.agentFactory = agentFactory;
    }

    /**
     * {@code Friendship} instances created by the supplied factory will be used
     * as edges in the social network constructed by this factory.
     */
    public void setFriendshipFactory(Factory<Friendship> friendshipFactory) {
        this.friendshipFactory = friendshipFactory;
    }

    public void setNumberOfAgents(int numberOfAgents) {
        this.numberOfAgents = numberOfAgents;
    }
}
