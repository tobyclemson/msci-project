package ic.msciproject.minoritygame.factories;

import cern.jet.random.AbstractDistribution;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import org.apache.commons.collections15.Factory;

/**
 * The RandomValueFactory returns values in a range at random and in a uniform
 * manner. An instance of RandomValueFactory is configured with a lower bound
 * and an upper bound and returns values in the range inclusive of the two
 * bounds.
 * @author tobyclemson
 */
public class RandomValueFactory implements Factory<Integer>{

    /**
     * An AbstractDistribution, containing a random number generator that
     * implements the RandomGenerator interface, which returns random numbers
     * according to a particular distribution. This is used to populate
     * strategies with random predictions. By default it is initialised with a
     * Uniform distribution backed by a MersenneTwister random number engine
     * initialised with a random integer seed.
     */
    private static AbstractDistribution randomNumberGenerator;

    // initialise the random number generator
    static {
        /*
         * generate an integer at random spanning the entire range of possible
         * integers as a seed
         */
        int randomSeed = (int) ((Math.random() - 0.5) * 2 * Integer.MAX_VALUE);

        /*
         * construct a uniform distribution backed by a MersenneTwister random
         * number generator using the random seed and set the static random
         * number generator to the constructed object.
         */
        randomNumberGenerator = new Uniform(new MersenneTwister(randomSeed));
    }

    /**
     * The lower bound of values that this random value factory can return.
     */
    private int lowerBound;
    
    /**
     * The upper bound of vaues that this random value factory can return.
     */
    private int upperBound;
    
    /**
     * Constructs a RandomValueFactory instance with the supplied lower and 
     * upper bounds.
     * @param lowerBound The lower bound for values returned by this factory.
     * @param upperBound The upper bound for values returned by this factory.
     */
    public RandomValueFactory(int lowerBound, int upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    /**
     * Returns the lower bound for values returned by this factory.
     * @return The lower bound of all values returned by this factory.
     */
    public int getLowerBound() {
        return this.lowerBound;
    }

    /**
     * Returns the upper bound for values returned by this factory.
     * @return The upper bound of all values returned by this factory.
     */
    public int getUpperBound() {
        return this.upperBound;
    }

    /**
     * Returns a value at random from the range specified at construction
     * inclusive of the endpoints. Each value is equally likely to be returned.
     * @return A value in the specified range at random.
     */
    public Integer create() {
        Double value = randomNumberGenerator.nextDouble() * (
            getUpperBound() - getLowerBound() + 1
        ) + getLowerBound();
        
        return value.intValue();
    }
}
