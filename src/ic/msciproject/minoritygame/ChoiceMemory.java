package ic.msciproject.minoritygame;

import java.util.List;

/**
 * The ChoiceMemory class wraps a ChoiceHistory object and represents the
 * memory of an agent. Each memory can have a different capacity which is the
 * number of past minority choices it can read back.
 * @author tobyclemson
 */
public class ChoiceMemory {

    /**
     * An integer representing the capacity of the choice memory.
     */
    public int capacity;

    /**
     * A ChoiceHistory object representing the global choice history containing
     * the past minority choices for every time step.
     */
    public ChoiceHistory choiceHistoryProvider;

    /**
     * Creates a ChoiceMemory instance backed by the supplied ChoiceHistory
     * instance and with the supplied capacity.
     * @param choiceHistoryProvider The ChoiceHistory object from which the
     * memory instance should draw its information.
     * @param capacity The number of steps back in time the memory has
     * capacity to remember.
     */
    public ChoiceMemory(
        ChoiceHistory choiceHistoryProvider,
        int capacity
    ) {
        this.choiceHistoryProvider = choiceHistoryProvider;
        this.capacity = capacity;
    }

    /**
     * Returns an integer representing the capacity of the memory object.
     * @return The capacity of the memory.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns the ChoiceHistory object backing the memory.
     * @return The choice history that the memory reads from.
     */
    public ChoiceHistory getChoiceHistoryProvider() {
        return choiceHistoryProvider;
    }

    /**
     * Returns the contents of the choice memory as a List of Choice
     * enumerations.
     * @return
     */
    public List<Choice> fetch() {
        return choiceHistoryProvider.asList(capacity);
    }

}
