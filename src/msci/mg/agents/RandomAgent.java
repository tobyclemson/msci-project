package msci.mg.agents;

import cern.jet.random.engine.*;
import cern.jet.random.*;
import msci.mg.Choice;

/**
 * The {@code RandomAgent} class represents a completely unintelligent agent
 * which makes a choice completely at random
 *
 * @author Toby Clemson
 */
public class RandomAgent extends AbstractAgent implements UnintelligentAgent {

    protected static AbstractDistribution randomNumberGenerator;

    static {
        /*
         * generate an integer at random spanning the entire range of possible
         * integers as a seed
         */
        int randomSeed = (int) ((Math.random() - 0.5) * 2 * Integer.MAX_VALUE);

        randomNumberGenerator = new Uniform(new MersenneTwister(randomSeed));
    }

    /**
     * Constructs a {@code RandomAgent} setting the strategy manager and
     * memory attributes to the supplied {@code StrategyManager} and
     * {@code ChoiceMemory} instances.
     */
    public RandomAgent() {
        super();
    }

    /**
     * Chooses randomly and uniformly between {@code Choice.A} and
     * {@code Choice.B}.
     */
    public void choose() {
        Choice currentChoice = null;

        if(randomNumberGenerator.nextInt() == 0) {
            currentChoice = Choice.A;
        } else {
            currentChoice = Choice.B;
        }

        this.choice = currentChoice;
    }
}
