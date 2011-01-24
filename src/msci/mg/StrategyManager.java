package msci.mg;

import cern.jet.random.AbstractDistribution;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;

import java.util.ArrayList;
import java.util.List;

public class StrategyManager {
    private static AbstractDistribution randomNumberGenerator;

    static {
        randomNumberGenerator = new Uniform(
                new MersenneTwister((int) ((Math.random() - 0.5) * 2 * Integer.MAX_VALUE)));
    }

    private List<Strategy> strategyStorage;
    private int strategyKeyLength;

    public StrategyManager(List<Strategy> strategies) {
        this.strategyStorage = strategies;
        this.strategyKeyLength = strategies.get(0).getKeyLength();
    }

    public int getNumberOfStrategies() {
        return strategyStorage.size();
    }

    public int getStrategyKeyLength() {
        return strategyKeyLength;
    }

    public List<Strategy> getStrategies() {
        return strategyStorage;
    }

    public Strategy getRandomStrategy() {
        return strategyStorage.get(getRandomIndex(strategyStorage.size()));
    }

    public Strategy getHighestScoringStrategy() {
        Strategy firstStrategy = strategyStorage.get(0);

        List<Strategy> highestScoringStrategies = new ArrayList<Strategy>();
        highestScoringStrategies.add(firstStrategy);

        int highestScore = firstStrategy.getScore();

        for (Strategy currentStrategy : strategyStorage.subList(1, strategyStorage.size())) {
            if (currentStrategy.getScore() > highestScore) {
                highestScoringStrategies.clear();
                highestScoringStrategies.add(currentStrategy);
                highestScore = currentStrategy.getScore();
            } else if (currentStrategy.getScore() == highestScore) {
                highestScoringStrategies.add(currentStrategy);
            }
        }

        return highestScoringStrategies.get(getRandomIndex(highestScoringStrategies.size()));
    }

    public void incrementScores(List<Choice> choiceHistory, Choice minorityChoice) {
        for (Strategy currentStrategy : strategyStorage) {
            Choice prediction = currentStrategy.predictMinorityChoice(choiceHistory);
            if (prediction.equals(minorityChoice)) {
                currentStrategy.incrementScore();
            }
        }
    }

    private int getRandomIndex(int arraySize) {
        return (int) (randomNumberGenerator.nextDouble() * arraySize);
    }
}
