package msci.mg.agents;

import cern.jet.random.engine.*;
import cern.jet.random.*;
import msci.mg.Choice;
import msci.mg.ChoiceMemory;
import msci.mg.StrategyManager;

/**
 * The RandomAgent class represents a completely unintelligent agent which
 * makes a choice completely at random based on the random number generator
 * associated with it.
 * @author tobyclemson
 */
public class RandomAgent extends AbstractAgent {

    /**
     * An AbstractDistribution, containing a random number generator that
     * implements the RandomGenerator interface, which returns random numbers
     * according to a particular distribution. This is used to populate
     * strategies with random predictions. By default it is initialised with a
     * Uniform distribution backed by a MersenneTwister random number engine
     * initialised with a random seed.
     */
    protected static AbstractDistribution randomNumberGenerator;

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
     * Constructs an instance of RandomAgent setting the strategy manager and
     * memory attributes to the supplied StrategyManager and ChoiceMemroy
     * instances.
     * @param strategyManager A StrategyManager instance that is not actually
     * used by the agent.
     * @param choiceMemory A ChoiceMemory instance that is not actually used by
     * the agent.
     */
    public RandomAgent(
        StrategyManager strategyManager,
        ChoiceMemory choiceMemory
    ) {
        super(strategyManager, choiceMemory);
    }

    /**
     * Constructs an instance of RandomAgent with the strategy manager and
     * memory attributes set to null.
     * TODO: sort this out during next refactor.
     */
    public RandomAgent() {
        super();
    }

    /**
     * Calculates the agent's choice by choosing between Choice.A and Choice.B
     * based on a random number returned by the random number generator
     * associated with the agent. If the random number evaluates to 0, Choice.A
     * is made, otherwise Choice.B is made.
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
