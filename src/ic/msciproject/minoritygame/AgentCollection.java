package ic.msciproject.minoritygame;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

/**
 * The AgentCollection class holds a collection of Agent objects extending the
 * collection with agent related functionality.
 * @author tobyclemson
 */
public class AgentCollection extends AbstractCollection<AbstractAgent> {

    /**
     * An ArrayList of Agent instances representing the agents held by the
     * AgentCollection.
     */
    private ArrayList<AbstractAgent> storage;

    /**
     * Constructs an empty AgentCollection. Agent objects can be later
     * added to the collection using the {@link #add(AbstractAgent)} method.
     */
    public AgentCollection() {
        storage = new ArrayList<AbstractAgent>();
    }

    /**
     * Constructs an AgentCollection instance copying across the agents
     * from the supplied AgentCollection instance.
     * @param otherAgentCollection The AgentCollection instance to be
     * copied.
     */
    public AgentCollection(AgentCollection otherAgentCollection) {
        ArrayList<AbstractAgent> other_storage = otherAgentCollection.storage;
        storage = new ArrayList<AbstractAgent>();
        Iterator<AbstractAgent> iterator = other_storage.iterator();
        while(iterator.hasNext()){
            add(iterator.next());
        }
    }

    /**
     * Returns an iterator over the agents stored in the AgentCollection.
     * @return An iterator over the agents.
     */
    public Iterator<AbstractAgent> iterator() {
        return storage.iterator();
    }

    /**
     * Returns an integer representing the number of agents in the
     * collection.
     * @return The number of agents in the collection.
     */
    public int size() {
        return storage.size();
    }

    /**
     * Adds an agent to the collection.
     * @param agent The agent to add.
     * @return {@code true} if the collection was modified.
     */
    @Override
    public boolean add(AbstractAgent agent) {
        return storage.add(agent);
    }

    /**
     * Returns a map of the possible choices Choice.A and Choice.B to the
     * number of agents that made that choice in the last step.
     * @return A map of choice totals.
     */
    public Map<Choice, Integer> getLastChoiceTotals() {
        // initialise required variables.
        HashMap<Choice, Integer> totals = new HashMap<Choice, Integer>();
        Iterator<AbstractAgent> agentIterator = storage.iterator();
        Choice choice = null;
        int choiceATotal = 0;
        int choiceBTotal = 0;

        // count the number of agents making the choices "0" and "1" in the
        // last step.
        while(agentIterator.hasNext()) {
            choice = agentIterator.next().getLastChoice();
            switch(choice) {
                case A:
                    choiceATotal++;
                    break;
                case B:
                    choiceBTotal++;
                    break;
            }
        }

        // set the totals for the choice '0' and '1'.
        totals.put(Choice.A, choiceATotal);
        totals.put(Choice.B, choiceBTotal);

        // return the totals map.
        return totals;
    }

    /**
     * Asks each agent in the collection to make a choice between "0" and "1"
     * using the supplied history string.
     * @param choiceHistory A List of Choice instances representing the
     * past minority choices.
     */
    public void makeChoices(List<Choice> choiceHistory) {
        // declare required variables
        Iterator<AbstractAgent> agentIterator;
        AbstractAgent agent;

        // get an iterator over all agents
        agentIterator = storage.iterator();

        // tell each agent to make a choice
        while(agentIterator.hasNext()) {
            agent = agentIterator.next();
            agent.choose(choiceHistory);
        }
    }

    /**
     * Increments the score of each agent that made the minority choice in the
     * last step.
     * @param minorityChoice The minority choice in the last step.
     */
    public void incrementScoresForChoice(Choice minorityChoice) {
        // declare required variables
        Iterator<AbstractAgent> agentIterator;
        AbstractAgent agent;

        // get an iterator over all agents
        agentIterator = storage.iterator();

        // call incrementScore on each agent that made the correct choice
        while(agentIterator.hasNext()) {
            agent = agentIterator.next();
            if(agent.getLastChoice().equals(minorityChoice)) {
                agent.incrementScore();
            }
        }
    }

    /**
     * Calls update on each stored agent.
     * @param minorityChoice The minority choice in the last step.
     * @param choiceHistory The past minority choices as at the start of the
     * last time step.
     */
    public void updateForChoice(
        Choice minorityChoice,
        List<Choice> choiceHistory
    ) {
        // declare required variables
        Iterator<AbstractAgent> agentIterator;

        // get an iterator over all agents
        agentIterator = storage.iterator();

        // call incrementScore on each agent that made the correct choice
        while(agentIterator.hasNext()) {
            agentIterator.next().update(minorityChoice, choiceHistory);
        }
    }
}
