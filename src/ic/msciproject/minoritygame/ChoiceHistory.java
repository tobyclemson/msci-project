package ic.msciproject.minoritygame;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

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
     * A List to hold the history of choices. The list is returned by the
     * {@link #asList} method.
     */
    private List<Choice> contents;

    /**
     * Constructs a ChoiceHistory instance initially containing the specified
     * number of randomly chosen choices.
     * @param initialLength The number of choices with which to initially
     * populate the history (at random).
     */
    public ChoiceHistory(int initialLength){
        Random indexGenerator = new Random();
        Choice choiceSet[] = {Choice.A, Choice.B};

        this.contents = new ArrayList<Choice>();

        for(int i = 0; i < initialLength; i++) {
            contents.add(choiceSet[indexGenerator.nextInt(2)]);
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
