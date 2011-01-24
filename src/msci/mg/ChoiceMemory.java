package msci.mg;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ChoiceMemory {
    private int capacity;
    private Deque<Choice> memory;

    public ChoiceMemory(int capacity, List<Choice> initialChoices) {
        int initialChoiceCount = initialChoices.size();

        if (initialChoiceCount < capacity) {
            throw new IllegalArgumentException(
                    "Insufficient initial choices in supplied choice list for the specified memory capacity.");
        }

        this.capacity = capacity;
        this.memory = new ArrayDeque<Choice>();

        for (int i = 0; i < capacity; i++) {
            memory.addFirst(initialChoices.get(initialChoiceCount - 1 - i));
        }
    }

    public int getCapacity() {
        return capacity;
    }

    public void add(Choice choice) {
        memory.removeFirst();
        memory.addLast(choice);
    }

    public List<Choice> fetch() {
        return new ArrayList<Choice>(memory);
    }

}
