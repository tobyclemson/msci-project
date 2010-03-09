package msci.mg.agents;

import edu.uci.ics.jung.graph.Graph;
import java.util.Collection;
import msci.mg.Choice;
import msci.mg.ChoiceMemory;
import msci.mg.Friendship;
import msci.mg.Neighbourhood;
import msci.mg.StrategyManager;

/**
 * The {@code AbstractNetworkedAgent} class provides a partial implementation
 * of the {@code NetworkedAgent} interface easing the burden of implementing
 * a networked agent.
 *
 * @author Toby Clemson
 */
public abstract class AbstractNetworkedAgent
    extends AbstractIntelligentAgent
    implements NetworkedAgent {
    
    protected Agent bestFriend;
    protected Neighbourhood neighbourhood;
    protected int correctPredictionCount = 0;
    protected Choice prediction = null;

    /**
     * Constructs an {@code AbstractNetworkedAgent} setting the strategy
     * manager and memory attributes to the supplied {@code StrategyManager}
     * and {@code ChoiceMemory} instances. If the supplied strategy manager
     * contains strategies with a key length not equal to the memory capacity,
     * an {@code IllegalArgumentException} is thrown.
     *
     * @param strategyManager A {@code StrategyManager} instance containing
     * this agent's strategies.
     * @param choiceMemory A {@code ChoiceMemory} instance representing this
     * agent's memory of past minority choices.
     */
    public AbstractNetworkedAgent(
        StrategyManager strategyManager,
        ChoiceMemory choiceMemory
    ) {
        super(strategyManager, choiceMemory);
    }

    /**
     * If no choice has been made yet then this method throws an
     * {@code IllegalStateException}.
     */
    public Agent getBestFriend() {
        if (bestFriend == null) {
            throw new IllegalStateException(
                "No choice has been made so this agent does not have a best " +
                "friend yet."
            );
        } else {
            return bestFriend;
        }
    }

    /**
     * If no neighbourhood has been set, this method throws an
     * {@code IllegalStateException}.
     */
    public Collection<? extends Agent> getFriends() {
        if (this.neighbourhood == null) {
            throw new IllegalStateException("No neighbourhood has been set.");
        }
        return this.neighbourhood.getFriends();
    }

    public Neighbourhood getNeighbourhood() {
        return neighbourhood;
    }

    /**
     * If no neighbourhood has been set, this method throws an
     * {@code IllegalStateException}.
     */
    public Graph<? extends Agent, Friendship> getSocialNetwork() {
        if (this.neighbourhood == null) {
            throw new IllegalStateException("No neighbourhood has been set.");
        }
        return neighbourhood.getSocialNetwork();
    }

    public void setNeighbourhood(Neighbourhood neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public int getCorrectPredictionCount() {
        return correctPredictionCount;
    }

    /**
     * If no prediction has been made yet, this method throws an
     * {@code IllegalStateException}.
     */
    public Choice getPrediction() {
        if (prediction == null) {
            throw new IllegalStateException("No prediction has been made yet");
        }
        return prediction;
    }

    /**
     * The default implementation adds 1 to the correct prediction count.
     */
    public void incrementCorrectPredictionCount() {
        correctPredictionCount += 1;
    }
}
