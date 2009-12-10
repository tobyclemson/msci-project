package ic.msciproject.minoritygame;

import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

/**
 * The AgentManager class holds a collection of AbstractAgent objects extending
 * the collection with agent related functionality.
 * @author tobyclemson
 */
public class AgentManager {

    /**
     * A List of Agent instances representing the agents held by the
     * AgentManager.
     */
    private List<AbstractAgent> agentStorage;

    /**
     * Constructs an AgentManager instance containing the supplied AbstractAgent
     * instances.
     * @param agents The agents that this agent manager should manage.
     */
    public AgentManager(List<AbstractAgent> agents) {
        this.agentStorage = agents;
    }

    /**
     * Returns the List of AbstractAgent instances managed by the AgentManager.
     * @return the agents managed by the agent manager.
     */
    public List<AbstractAgent> getAgents() {
        return agentStorage;
    }

    /**
     * Returns an integer representing the number of agents stored by the
     * agent manager.
     * @return The number of agents stored by the agent manager.
     */
    public int getNumberOfAgents() {
        return agentStorage.size();
    }

    /**
     * Returns a map of the possible choices Choice.A and Choice.B to the
     * number of agents that made that choice in the last step.
     * @return A map of choice totals.
     */
    public Map<Choice, Integer> getLastChoiceTotals() {
        // initialise required variables.
        HashMap<Choice, Integer> totals = new HashMap<Choice, Integer>();
        Iterator<AbstractAgent> agentIterator = agentStorage.iterator();
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
     * Asks each agent managed by the AgentManager to make a choice between
     * Choice.A and Choice.B using the supplied choice history.
     * @param choiceHistory A List of Choice instances representing the
     * past minority choices.
     */
    public void makeChoices(List<Choice> choiceHistory) {
        // declare required variables
        Iterator<AbstractAgent> agentIterator;
        AbstractAgent agent;

        // get an iterator over all agents
        agentIterator = agentStorage.iterator();

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
        agentIterator = agentStorage.iterator();

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
        agentIterator = agentStorage.iterator();

        // call incrementScore on each agent that made the correct choice
        while(agentIterator.hasNext()) {
            agentIterator.next().update(minorityChoice, choiceHistory);
        }
    }
}
