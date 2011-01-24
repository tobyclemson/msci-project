package msci.mg;

import cern.jet.random.AbstractDistribution;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class StrategySpace {
    private static AbstractDistribution randomNumberGenerator;

    static {
        randomNumberGenerator = new Uniform(
                new MersenneTwister((int) ((Math.random() - 0.5) * 2 * Integer.MAX_VALUE)));
    }

    private int keyLength;
    private Set<List<Choice>> requiredKeys;
    private static final Choice choiceSet[] = {Choice.A, Choice.B};

    public StrategySpace(int keyLength) {
        this.keyLength = keyLength;
        this.requiredKeys = ChoiceListPermutator.generateAll(keyLength);
    }

    public int getKeyLength() {
        return keyLength;
    }

    public void setKeyLength(int keyLength) {
        this.keyLength = keyLength;
        this.requiredKeys = ChoiceListPermutator.generateAll(keyLength);
    }

    public Strategy generateStrategy() {
        HashMap<List<Choice>, Choice> mappings = new HashMap<List<Choice>, Choice>();

        for (List<Choice> key : requiredKeys) {
            mappings.put(key, choiceSet[randomNumberGenerator.nextInt()]);
        }

        return new Strategy(mappings);
    }
}
