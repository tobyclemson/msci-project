package msci.mg.agents;

import edu.uci.ics.jung.graph.Graph;
import msci.mg.*;

import java.util.Collection;

public abstract class AbstractNetworkedAgent extends AbstractIntelligentAgent implements NetworkedAgent {
    protected Agent bestFriend;
    protected Neighbourhood neighbourhood;
    protected Choice prediction = null;
    protected int correctPredictionCount = 0;

    public AbstractNetworkedAgent(
            StrategyManager strategyManager,
            ChoiceMemory choiceMemory) {
        super(strategyManager, choiceMemory);
    }

    public Agent getBestFriend() {
        if (bestFriend == null) {
            throw new IllegalStateException("No choice has been made so this agent does not have a best friend yet.");
        } else {
            return bestFriend;
        }
    }

    public Collection<? extends Agent> getFriends() {
        if (this.neighbourhood == null) {
            throw new IllegalStateException("No neighbourhood has been set.");
        }
        return this.neighbourhood.getFriends();
    }

    public Neighbourhood getNeighbourhood() {
        return neighbourhood;
    }

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

    public Choice getPrediction() {
        if (prediction == null) {
            throw new IllegalStateException("No prediction has been made yet");
        }
        return prediction;
    }

    public void incrementCorrectPredictionCount() {
        correctPredictionCount += 1;
    }
}
