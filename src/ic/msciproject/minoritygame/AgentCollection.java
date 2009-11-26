package ic.msciproject.minoritygame;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
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
     * Returns a map of the possible choices '0' and '1' to the number of agents
     * that made that choice in the last step.
     * @return A map of choice totals.
     */
    public Map<String, Integer> getLastChoiceTotals() {
        // initialise required variables.
        HashMap<String, Integer> totals = new HashMap<String, Integer>();
        Iterator<AbstractAgent> agentIterator = storage.iterator();
        String choice = "";
        int choiceZeroTotal = 0;
        int choiceOneTotal = 0;

        // count the number of agents making the choices "0" and "1" in the
        // last step.
        while(agentIterator.hasNext()) {
            choice = agentIterator.next().getLastChoice();
            if(choice.equals("0")) {
                choiceZeroTotal += 1;
            } else if(choice.equals("1")) {
                choiceOneTotal += 1;
            }
        }

        // set the totals for the choice '0' and '1'.
        totals.put("0", choiceZeroTotal);
        totals.put("1", choiceOneTotal);

        // return the totals map.
        return totals;
    }

    /**
     * Asks each agent in the collection to make a choice between "0" and "1"
     * using the supplied history string.
     * @param historyString The history string of minority choices in recent
     * steps required by the agents to make a choice.
     */
    public void makeChoices(String historyString) {
        // declare required variables
        Iterator<AbstractAgent> agentIterator;
        AbstractAgent agent;

        // get an iterator over all agents
        agentIterator = storage.iterator();

        // tell each agent to make a choice
        while(agentIterator.hasNext()) {
            agent = agentIterator.next();
            agent.makeChoice(historyString);
        }
    }

    public void incrementScoresForChoice(String minorityChoice) {
        // declare required variables
        Iterator<AbstractAgent> agentIterator;
        AbstractAgent agent;

        // get an iterator over all agents
        agentIterator = storage.iterator();

        // call incrementScore on each agent that made the correct choice
        while(agentIterator.hasNext()) {
            agent = agentIterator.next();
            if(agent.getLastChoice().equals(minorityChoice))
                agent.incrementScore();
        }
    }

    
    public void updateForChoice(String minorityChoice, String historyString) {
        // declare required variables
        Iterator<AbstractAgent> agentIterator;

        // get an iterator over all agents
        agentIterator = storage.iterator();

        // call incrementScore on each agent that made the correct choice
        while(agentIterator.hasNext()) {
            agentIterator.next().updateForChoice(minorityChoice, historyString);
        }
    }
}
