package ic.msciproject.minoritygame;

import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.Iterator;

/**
 * The Strategy class represents a strategy employed by an agent in predicting
 * the next outcome in the minority game. A Strategy instance is initialised
 * with a map of choice permutations to predicted outcomes and when an agent has
 * to choose an outcome, the choice history is used to retrieve the predicted
 * outcome for that choice permutation from the Strategy.
 * @author tobyclemson
 */
public class Strategy {

    /**
     * A Map of Lists of Choice objects to Choices. The entries in the map
     * represent predictions of the next minority choice for all possible
     * permutations of choice histories of the length associated with this
     * strategy.
     */
    protected Map<List<Choice>, Choice> strategyMap;

    /**
     * An integer representing the key length in the mapping of the
     * Strategy. This is returned by the {@link #getKeyLength} method.
     */
    protected int keyLength;

    /**
     * An integer representing the score of the Strategy. The score of a
     * Strategy is incremented if in a given turn it would have resulted in
     * a correct prediction for the minority choice. The score is incremented
     * using the {@link #incrementScore} method and returned by the
     * {@link #getScore} method.
     */
    protected int score = 0;

    /**
     * A Set containing all of the possible permutations of choices valid
     * as input keys for the strategy.
     */
    protected Set<List<Choice>> validChoiceHistories;

    /**
     * Constructs a Strategy instance using the supplied history string to
     * outcome mappings.
     * <p>
     * The supplied mappings must all have the same key length and must
     * represent every possible mapping for that key lengthIf this is not true,
     * an IllegalArgumentException is thrown.
     * 
     * @param strategyMap A map of all possible input keys of the required
     * length to predictions for the minority choice given that input key.
     */
    public Strategy(Map<List<Choice>, Choice> strategyMap) {
        // throw an IllegalArgumentException if the supplied map contains no
        // entries
        if(strategyMap.isEmpty()) {
            throw new IllegalArgumentException(
                "The supplied Map cannot be empty"
            );
        }

        // declare required variables
        Set<List<Choice>> keySet;
        Iterator<List<Choice>> keyIterator;
        List<Choice> currentKey;

        int previousKeyLength, currentKeyLength;

        // predictMinorityChoice an iterator for the set of keys contained in the supplied
        // mapping
        keySet = strategyMap.keySet();
        keyIterator = keySet.iterator();

        // initialise variables
        currentKey = null;
        currentKeyLength = 0;
        previousKeyLength = 0;

        // check that all keys in the mapping are the same length, throwing
        // an IllegalArgumentException if any differ.
        do {
            currentKey = keyIterator.next();
            currentKeyLength = currentKey.size();

            if(previousKeyLength == 0) {
                previousKeyLength = currentKeyLength;
            } else if(currentKeyLength != previousKeyLength) {
                throw new IllegalArgumentException(
                    "The keys in the supplied Map must all be of the same " +
                    "length"
                );
            }
        } while(keyIterator.hasNext());

        // set the key length to that found in the supplied mappings
        keyLength = currentKeyLength;

        // if the supplied mapping does not contain all of the required keys
        // throw an IllegalArgumentException.
        validChoiceHistories = ChoiceListPermutator.generateAll(keyLength);

        if(!keySet.equals(validChoiceHistories)) {
            throw new IllegalArgumentException(
                "The set of keys in the supplied map must include all " +
                "possible permutations of Choice.A and Choice.B of the " + 
                "required length."
            );
        }

        // if all is well, set the mappings to the supplied mappings object.
        this.strategyMap = strategyMap;
    }

    /**
     * Returns the length of the keys contained in the mappings in this
     * strategy.
     * @return An integer representing the key length.
     */
    public int getKeyLength() {
        return keyLength;
    }

    public Set<List<Choice>> getValidChoiceHistories() {
        return validChoiceHistories;
    }

    /**
     * Returns the score of the Strategy. The score represents the number of
     * steps in the game so far for which the strategy has made correct
     * predictions.
     * @return The score of the strategy.
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the map of choice histories to choices representing the strategy
     * used in predicting the next minority choice.
     * @return The map of choice histories to predictions comprising the
     * strategy.
     */
    public Map<List<Choice>, Choice> getMap() {
        return strategyMap;
    }

    /**
     * Returns the outcome corresponding to the supplied history
     * string predicted by the Strategy.
     * @param choiceHistory A fixed size list of past minority choices (most
     * recent has highest index) with the size corresponding to the length of
     * keys in the strategy.
     * @return The predicted outcome for the supplied choiceHistory, either
     * Choice.A or Choice.B.
     */
    public Choice predictMinorityChoice(List<Choice> choiceHistory) {
        if(!validChoiceHistories.contains(choiceHistory)) {
            throw new IllegalArgumentException(
                "The strategy does not contain a prediction for the choice" +
                "history: " + choiceHistory
            );
        }
        return strategyMap.get(choiceHistory);
    }

    /**
     * Increments the Strategy's score by 1.
     */
    public void incrementScore() {
        score += 1;
    }
}
