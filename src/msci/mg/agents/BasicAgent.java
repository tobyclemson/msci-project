package msci.mg.agents;

import msci.mg.Choice;
import msci.mg.ChoiceMemory;
import msci.mg.Strategy;
import msci.mg.StrategyManager;

public class BasicAgent extends AbstractIntelligentAgent {
    public BasicAgent(
            StrategyManager strategyManager,
            ChoiceMemory choiceMemory) {
        super(strategyManager, choiceMemory);
    }

    public void choose() {
        Strategy chosenStrategy;

        if (choice == null) {
            chosenStrategy = strategyManager.getRandomStrategy();
        } else {
            chosenStrategy = strategyManager.getHighestScoringStrategy();
        }

        choice = chosenStrategy.predictMinorityChoice(memory.fetch());
    }

    @Override
    public void update(Choice correctChoice) {
        super.update(correctChoice);
        strategyManager.incrementScores(memory.fetch(), correctChoice);
        memory.add(correctChoice);
    }
}
