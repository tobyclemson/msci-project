package msci.mg.factories;

import cern.jet.random.AbstractDistribution;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import org.apache.commons.collections15.Factory;

public class RandomValueFactory implements Factory<Integer> {
    private static AbstractDistribution randomNumberGenerator;

    static {
        randomNumberGenerator = new Uniform(
                new MersenneTwister((int) ((Math.random() - 0.5) * 2 * Integer.MAX_VALUE)));
    }

    private int lowerBound;
    private int upperBound;

    public RandomValueFactory(int lowerBound, int upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public int getLowerBound() {
        return this.lowerBound;
    }

    public int getUpperBound() {
        return this.upperBound;
    }

    public Integer create() {
        Double value = randomNumberGenerator.nextDouble() * (getUpperBound() - getLowerBound() + 1) + getLowerBound();
        return value.intValue();
    }
}
