package ic.msciproject.minoritygame;

import java.util.Map;
import java.util.List;

/**
 * The AbstractMinorityGame class implements the basic functionality of a
 * minority game and all specific versions of the minority game should extend
 * it.
 * @author tobyclemson
 */
public class AbstractMinorityGame {

    /**
     * An AgentManager instance holding AbstractAgent instances used to
     * represent the agentManager associated with the minority game. This is returned
     * by the {@link #getAgents} method.
     */
    private AgentManager agentManager;

    /**
     * A ChoiceHistory instance holding a list of choices representing the
     * entire history of recent outcomes in the minority game. This is returned
     * by the {@link #getChoiceHistory} method.
     */
    private ChoiceHistory choiceHistory;

    /**
     * An integer representing the size of the memory of each agent in the game.
     */
    private int agentMemorySize;

    /**
     * Constructs an AbstractMinorityGame instance setting the agentManager, choice
     * history and agent memory size attributes to the supplied AgentManager,
     * ChoiceHistory and integer instances.
     * @param agentManager An AgentManager instance containing the agentManager
     * associated with this minority game instance.
     * @param choiceHistory A ChoiceHistory instance to use as the history
     * of outcomes for this minority game instance.
     * @param agentMemorySize An integer representing the number of past
     * minority choices each agent can remember.
     */
    public AbstractMinorityGame(
        AgentManager agentManager,
        ChoiceHistory choiceHistory,
        int agentMemorySize
    ){
        this.agentManager = agentManager;
        this.choiceHistory = choiceHistory;
        this.agentMemorySize = agentMemorySize;
    }

    /**
     * Returns a List of AbstractAgent instances representing the agents
     * associated with the minority game.
     * @return The agents associated with the minority game.
     */
    public List<AbstractAgent> getAgents() {
        return agentManager.getAgents();
    }

    /**
     * Returns the associated AgentManager instance.
     * @return The associated agent manager.
     */
    public AgentManager getAgentManager() {
        return agentManager;
    }

    /**
     * Returns a ChoiceHistory instance containing a list of Choice values
     * representing the past minority choices in the minority game.
     * @return A ChoiceHistory instance representing the history of outcomes
     * for the minority game.
     */
    public ChoiceHistory getChoiceHistory() {
        return choiceHistory;
    }

    /**
     * Returns an integer representing the number of past minority choices each
     * agent in the game can remember.
     * @return The number of past minority choices each agent in the game can
     * remember.
     */
    public int getAgentMemorySize() {
        return agentMemorySize;
    }

    /**
     * Returns the size of the minority group in the last step.
     * @return The size of the minority group.
     */
    public int getLastMinoritySize() {
        // declare variables
        Map<Choice, Integer> lastChoiceTotals;
        Integer numberChoosingA, numberChoosingB;
        
        // get a mapping of the choices '0' and '1' to the number of agentManager
        // making that choice in the last step.
        lastChoiceTotals = agentManager.getLastChoiceTotals();
        
        // retrieve the numbers of agentManager making each choice.
        numberChoosingA = lastChoiceTotals.get(Choice.A);
        numberChoosingB = lastChoiceTotals.get(Choice.B);

        // return the smallest of those numbers.
        return Math.min(numberChoosingA, numberChoosingB);
    }

    /**
     * Returns the minority choice in the last step.
     * @return The minority choice in the last step.
     */
    public Choice getLastMinorityChoice() {
        // declare variables
        Map<Choice, Integer> lastChoiceTotals;
        Integer numberChoosingA, numberChoosingB;

        // get a mapping of the choices '0' and '1' to the number of agentManager
        // making that choice in the last step.
        lastChoiceTotals = agentManager.getLastChoiceTotals();

        // retrieve the numbers of agentManager making each choice.
        numberChoosingA = lastChoiceTotals.get(Choice.A);
        numberChoosingB = lastChoiceTotals.get(Choice.B);

        // return the smallest of those numbers.
        if(numberChoosingA < numberChoosingB) {
            return Choice.A;
        }
        else {
            return Choice.B;
        }
    }

    /**
     * Takes a step forward in the game.
     *
     * The step is taken as follows:
     * <ul>
     *  <li>Tell all agentManager to make a choice for this time step
     *  <li>Increment the scores of all agentManager that made the minority choice
     *  <li>Tell all agentManager to update their local information given the
     *      last minority choice and choice history
     *  <li>Add the most recent minority choice outcome to the choice history
     * </ul>
     */
    public void stepForward() {
        // tell all agentManager to make a choice for this time step
        agentManager.makeChoices(
            choiceHistory.asList(agentMemorySize)
        );

        // retrieve the minority choice
        Choice minorityChoice = getLastMinorityChoice();

        // update based on the minority choice by incrementing agent scores,
        // telling agentManager to update and updating the choice history
        agentManager.incrementScoresForChoice(minorityChoice);
        agentManager.updateForChoice(
            minorityChoice,
            choiceHistory.asList(agentMemorySize)
        );
        choiceHistory.add(minorityChoice);
    }
}
