package msci.mg;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Strategy {
    private Map<List<Choice>, Choice> strategyMap;
    private Set<List<Choice>> validChoiceHistories;
    private int keyLength;
    private int score = 0;

    public Strategy(Map<List<Choice>, Choice> strategyMap) {
        if (strategyMap.isEmpty()) {
            throw new IllegalArgumentException("The supplied Map cannot be empty");
        }

        Set<List<Choice>> keySet = strategyMap.keySet();

        int previousKeyLength, currentKeyLength;

        currentKeyLength = 0;
        previousKeyLength = 0;

        for (List<Choice> currentKey : keySet) {
            currentKeyLength = currentKey.size();

            if (previousKeyLength == 0) {
                previousKeyLength = currentKeyLength;
            } else if (currentKeyLength != previousKeyLength) {
                throw new IllegalArgumentException("The keys in the supplied Map must all be of the same length");
            }
        }

        keyLength = currentKeyLength;
        validChoiceHistories = ChoiceListPermutator.generateAll(keyLength);

        if (!keySet.equals(validChoiceHistories)) {
            throw new IllegalArgumentException("The set of keys in the supplied map must include all " +
                    "possible permutations of Choice.A and Choice.B of the " +
                    "required length.");
        }

        this.strategyMap = strategyMap;
    }

    public int getKeyLength() {
        return keyLength;
    }

    public Set<List<Choice>> getValidChoiceHistories() {
        return validChoiceHistories;
    }

    public int getScore() {
        return score;
    }

    public Map<List<Choice>, Choice> getMap() {
        return strategyMap;
    }

    public Choice predictMinorityChoice(List<Choice> choiceHistory) {
        if (!validChoiceHistories.contains(choiceHistory)) {
            throw new IllegalArgumentException(
                    "The strategy does not contain a prediction for the choice history: " + choiceHistory);
        }
        return strategyMap.get(choiceHistory);
    }

    public void incrementScore() {
        score += 1;
    }
}
