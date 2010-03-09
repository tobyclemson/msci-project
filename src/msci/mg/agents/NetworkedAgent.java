package msci.mg.agents;

import edu.uci.ics.jung.graph.Graph;
import java.util.Collection;
import msci.mg.Choice;
import msci.mg.Friendship;
import msci.mg.Neighbourhood;

/**
 * The {@code NetworkedAgent} interface represents an {@code IntelligentAgent}
 * that can communicate with other {@code NetworkedAgent}s to which it is
 * connected in a social network. This allows the agent to communicate its
 * predictions and make choices based on the predictions of its friends.
 *
 * @author Toby Clemson
 */
public interface NetworkedAgent extends IntelligentAgent {
    /**
     * This method requires that a {@code Neighbourhood} instance has been set
     * for this agent. If no neighbourhood has been set, an
     * {@code IllegalStateException} is thrown.
     */
    Graph<? extends Agent, Friendship> getSocialNetwork();

    /**
     * An agent's best friend is defined as the agent whose prediction the
     * agent most recently followed.
     */
    Agent getBestFriend();

    Collection<? extends Agent> getFriends();
    Neighbourhood getNeighbourhood();
    void setNeighbourhood(Neighbourhood neighbourhood);

    /**
     * If no prediction has been made yet, this method throws an
     * {@code IllegalStateException}.
     */
    Choice getPrediction();

    int getCorrectPredictionCount();
    void incrementCorrectPredictionCount();
}
