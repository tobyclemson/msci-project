package msci.mg.agents;

import msci.mg.Choice;
import msci.mg.ChoiceMemory;
import msci.mg.Strategy;
import msci.mg.StrategyManager;

/**
 * The (@code BasicNetworkedAgent} class represents an agent that utilises
 * local information as well as global information when making a decision about
 * which choice to make at each step in the minority game. A basic networked
 * agent has access to information from a number of other agents also involved
 * in the game. At each time step, the agent makes a choice based on its peers
 * suggestions and past success.
 *
 * @author Toby Clemson
 */
public class BasicNetworkedAgent 
    extends AbstractNetworkedAgent {

    /**
     * Constructs a {@code BasicAgent} setting the strategy manager and
     * memory attributes to the supplied {@code StrategyManager} and
     * {@code ChoiceMemory} instances.
     *
     * @param strategyManager A {@code StrategyManager} instance representing
     * this agent's strategies.
     * @param choiceMemory A {@code ChoiceMemory} instance representing this
     * agent's memory of past minority choices.
     */
    public BasicNetworkedAgent(
        StrategyManager strategyManager,
        ChoiceMemory choiceMemory
    ) {
        super(strategyManager, choiceMemory);
    }

    /**
     * After calling prepare, the {@link #getPrediction()} method returns this
     * agent's preduction for the correct choice in this turn.
     */
    @Override public void prepare() {
        Strategy chosenStrategy;

        if(prediction == null) {
            chosenStrategy = strategyManager.getRandomStrategy();
        }
        else {
            chosenStrategy = strategyManager.getHighestScoringStrategy();
        }

        prediction = chosenStrategy.predictMinorityChoice(memory.fetch());
    }

    /**
     * Calculates this agent's choice based on the predictions of its friends
     * in the social network.
     */
    @Override public void choose() {
        Agent newBestFriend =
            neighbourhood.getMostSuccessfulPredictor();

        if(!(newBestFriend instanceof NetworkedAgent)) {
            this.choice = this.prediction;
            this.bestFriend = this;
        } else {
            NetworkedAgent networkedNewBestFriend =
                (NetworkedAgent) newBestFriend;
            this.choice = networkedNewBestFriend.getPrediction();
            this.bestFriend = newBestFriend;
        }
    }

    /**
     * Updates this agent's local information with respect to the correct
     * choice.
     * 
     * @param correctChoice The correct choice for this turn.
     */
    @Override public void update(Choice correctChoice) {
        super.update(correctChoice);

        if(correctChoice == prediction) {
            incrementCorrectPredictionCount();
        }

        strategyManager.incrementScores(memory.fetch(), correctChoice);
        memory.add(correctChoice);
    }
}
