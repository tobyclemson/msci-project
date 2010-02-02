package ic.msciproject.minoritygame;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * The ChoiceMemory class represents the memory of an agent. Each memory can
 * have a different capacity which is the number of past minority choices it
 * can remember.
 * @author tobyclemson
 */
public class ChoiceMemory {

    /**
     * An integer representing the capacity of the choice memory.
     */
    private int capacity;

    /**
     * A list of choices representing the contents of this memory.
     */
    private Deque<Choice> memory;

    /**
     * Creates a ChoiceMemory instance with the supplied capacity and initially
     * populated with the supplied list of choices. If the list of choices 
     * contains fewer choices than the specified memory capacity, an
     * IllegalArgumentException is thrown. If the list of choices contains
     * more choices than the specified memory capacity then the highest index
     * entries are considered most recent and favoured over the lowers index
     * entries.
     * @param capacity The number of minority choices back in time the memory
     * has capacity to hold.
     * @param initialChoices A list of choices with which to initially populate
     * the memory.
     */
    public ChoiceMemory(
        int capacity, List<Choice> initialChoices
    ) {
        // if there are too few initial choices, throw an
        // IllegalArgumentException
        int initialChoiceCount = initialChoices.size();
        if(initialChoiceCount < capacity){
            throw new IllegalArgumentException(
                "Insufficient initial choices in supplied choice list for " +
                "the specified memory capacity."
            );
        }

        // set the capacity and store the supplied initialChoices
        this.capacity = capacity;
        this.memory = new ArrayDeque<Choice>();
        for(int i = 0; i < capacity; i++) {
            memory.addFirst(initialChoices.get(initialChoiceCount - 1 - i));
        }
    }

    /**
     * Returns an integer representing the capacity of the memory object.
     * @return The capacity of the memory.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Adds the supplied choice to this memory as the most recent entry.
     * @param choice The choice to add to the memory.
     */
    public void add(Choice choice) {
        memory.removeFirst();
        memory.addLast(choice);
    }

    /**
     * Returns the contents of this choice memory as a List of Choice
     * enumerations.
     * @return The contents of this memory as a list of choices.
     */
    public List<Choice> fetch() {
        return new ArrayList<Choice>(memory);
    }

}
