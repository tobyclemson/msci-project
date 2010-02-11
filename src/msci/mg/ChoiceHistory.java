package msci.mg;

import java.util.List;
import java.util.ArrayList;
import cern.jet.random.AbstractDistribution;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;

/**
 * The ChoiceHistory class encapsulates the concept of the global history of 
 * minority choices in the minority game. The choice history expands to hold as
 * many choices as are appended to it and is used by agents who can read a
 * fixed number of the past outcomes from the end of the list through
 * their fixed size memories.
 * @author tobyclemson
 */
public class ChoiceHistory {

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
         * integers (except the endpoints) to be used as a seed
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
     * A List to hold the history of choices. The list is returned by the
     * {@link #asList} method.
     */
    private List<Choice> contents;

    /**
     * An array of the possible choices in the game.
     */
    private static final Choice choiceSet[] = {Choice.A, Choice.B};

    /**
     * Constructs a ChoiceHistory instance initially containing the specified
     * number of randomly chosen choices.
     * @param initialLength The number of choices with which to initially
     * populate the history (at random).
     */
    public ChoiceHistory(int initialLength){
        this.contents = new ArrayList<Choice>(initialLength);

        for(int i = 0; i < initialLength; i++) {
            contents.add(choiceSet[randomNumberGenerator.nextInt()]);
        }
    }

    /**
     * Returns the number of past choices held in the history as an integer.
     * @return The number of past choices in the history string.
     */
    public int getSize(){
        return contents.size();
    }

    /**
     * Converts the ChoiceHistory object into a List of Choice instances and
     * returns it.
     * @return The ChoiceHistory as a List of Choice instances.
     */
    public List<Choice> asList() {
        return contents;
    }

    /**
     * Returns the specified number of most recent choices from the history as
     * a List.
     * @param distanceIntoThePast The number of most recent choices to return.
     * @return A list of the specified number of most recent choices.
     */
    public List<Choice> asList(int distanceIntoThePast) {
        int size = contents.size();
        return contents.subList(
            size - distanceIntoThePast,
            size
        );
    }

    /**
     * Adds the supplied choice to the choice history.
     * @param choice The choice to add to the choice history.
     */
    public void add(Choice choice) {
        contents.add(choice);
    }
}
