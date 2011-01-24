package msci.mg.agents;

import msci.mg.Choice;
import msci.mg.ChoiceMemory;
import msci.mg.Strategy;
import msci.mg.StrategyManager;

public class BasicNetworkedAgent extends AbstractNetworkedAgent {
    public BasicNetworkedAgent(StrategyManager strategyManager, ChoiceMemory choiceMemory) {
        super(strategyManager, choiceMemory);
    }

    @Override
    public void prepare() {
        Strategy chosenStrategy;

        if (prediction == null) {
            chosenStrategy = strategyManager.getRandomStrategy();
        } else {
            chosenStrategy = strategyManager.getHighestScoringStrategy();
        }

        prediction = chosenStrategy.predictMinorityChoice(memory.fetch());
    }

    @Override
    public void choose() {
        Agent newBestFriend = neighbourhood.getMostSuccessfulPredictor();

        if (!(newBestFriend instanceof NetworkedAgent)) {
            this.choice = this.prediction;
            this.bestFriend = this;
        } else {
            NetworkedAgent networkedNewBestFriend = (NetworkedAgent) newBestFriend;
            this.choice = networkedNewBestFriend.getPrediction();
            this.bestFriend = newBestFriend;
        }
    }

    @Override
    public void update(Choice correctChoice) {
        super.update(correctChoice);

        if (correctChoice == prediction) {
            incrementCorrectPredictionCount();
        }

        strategyManager.incrementScores(memory.fetch(), correctChoice);
        memory.add(correctChoice);
    }
}
