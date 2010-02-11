package msci.mg.factories;

import edu.uci.ics.jung.graph.Graph;
import msci.mg.AbstractAgent;
import msci.mg.Friendship;
import org.apache.commons.collections15.Factory;

/**
 * The SocialNetworkFactory is an abstract factory at the top of a hierarchy
 * of concrete implementations. Each subclass must override the {@link
 * #create()} method and has access to an agent factory and a friendship
 * factory which create the agents and friendships to be used as vertices and
 * edges in the resulting graph.
 * @author tobyclemson
 */
public abstract class SocialNetworkFactory
    implements Factory<Graph<AbstractAgent, Friendship>> {

    /**
     * The agent factory which generate agents to be used as vertices in the
     * social network.
     */
    private Factory<AbstractAgent> agentFactory;

    /**
     * The friendship factory which generate friendships to be used as edges in
     * the social network.
     */
    private Factory<Friendship> friendshipFactory;

    /**
     * The number of agents required in the social network created by this
     * factory.
     */
    private int numberOfAgents;

    /**
     * Construct a SocialNetworkFactory setting the agent and friendship
     * factories to the supplied factories and setting the number of agents to
     * be built in the social network to the supplied value.
     * @param agentFactory A factory that creates agents for use as vertices in
     * the social network.
     * @param friendshipFactory A factory that creates friendships for use as
     * edges in the social network.
     * @param numberOfAgents The number of agents required in the social
     * network.
     */
    public SocialNetworkFactory(
        Factory<AbstractAgent> agentFactory,
        Factory<Friendship> friendshipFactory,
        int numberOfAgents
    ) {
        this.agentFactory = agentFactory;
        this.friendshipFactory = friendshipFactory;
        this.numberOfAgents = numberOfAgents;
    }

    /**
     * Returns the agent factory associated with this social network factory.
     * @return The associated agent factory.
     */
    public Factory<AbstractAgent> getAgentFactory() {
        return this.agentFactory;
    }

    /**
     * Returns the friendship factory associated with this social network
     * factory.
     * @return The associated friendship factory.
     */
    public Factory<Friendship> getFriendshipFactory() {
        return this.friendshipFactory;
    }

    /**
     * Returns the number of agents that this factory will build into the social
     * networks that it creates.
     * @return The number of agents that will be build into each social network.
     */
    public int getNumberOfAgents() {
        return this.numberOfAgents;
    }

    /**
     * Sets the agent factory to the supplied factory.
     * @param agentFactory A factory that creates agents for use as vertices in
     * the social network.
     */
    public void setAgentFactory(Factory<AbstractAgent> agentFactory) {
        this.agentFactory = agentFactory;
    }

    /**
     * Sets the friendship factory to the supplied factory.
     * @param friendshipFactory A factory that creates friendships for use as
     * edges in the social network.
     */
    public void setFriendshipFactory(Factory<Friendship> friendshipFactory) {
        this.friendshipFactory = friendshipFactory;
    }

    /**
     * Sets the required number of agents to the supplied value.
     * @param numberOfAgents The number of agents that each social network
     * built by this factory should have.
     */
    public void setNumberOfAgents(int numberOfAgents) {
        this.numberOfAgents = numberOfAgents;
    }

    /**
     * Creates a social network.
     * @return A social network with agents as vertices and friendships as
     * edges.
     */
    public abstract Graph<AbstractAgent, Friendship> create();

}
