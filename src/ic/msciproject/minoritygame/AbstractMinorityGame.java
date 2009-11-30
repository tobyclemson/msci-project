package ic.msciproject.minoritygame;

import java.util.Map;

/**
 * The AbstractMinorityGame class implements the basic functionality of a
 * minority game and all specific versions of the minority game should extend
 * it.
 * @author tobyclemson
 */
public class AbstractMinorityGame {

    /**
     * An AgentCollection instance holding AbstractAgent instances used to
     * represent the agents associated with the minority game. This is returned
     * by the {@link #getAgents} method.
     */
    private AgentCollection agents;

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
     * Constructs an AbstractMinorityGame instance setting the agents, choice
     * history and agent memory size attributes to the supplied AgentCollection,
     * ChoiceHistory and integer instances.
     * @param agents An AgentCollection instance containing the agents
     * associated with this minority game instance.
     * @param choiceHistory A ChoiceHistory instance to use as the history
     * of outcomes for this minority game instance.
     * @param agentMemorySize An integer representing the number of past
     * minority choices each agent can remember.
     */
    public AbstractMinorityGame(
        AgentCollection agents,
        ChoiceHistory choiceHistory,
        int agentMemorySize
    ){
        this.agents = agents;
        this.choiceHistory = choiceHistory;
        this.agentMemorySize = agentMemorySize;
    }

    /**
     * Returns an AgentCollection of derivatives of AbstractAgent representing
     * the agents associated with this instance of the minority game.
     * @return An AgentCollection containing the agents associated with the
     * minority game.
     */
    public AgentCollection getAgents() {
        return agents;
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
        
        // get a mapping of the choices '0' and '1' to the number of agents
        // making that choice in the last step.
        lastChoiceTotals = agents.getLastChoiceTotals();
        
        // retrieve the numbers of agents making each choice.
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

        // get a mapping of the choices '0' and '1' to the number of agents
        // making that choice in the last step.
        lastChoiceTotals = agents.getLastChoiceTotals();

        // retrieve the numbers of agents making each choice.
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
     *  <li>Tell all agents to make a choice for this time step
     *  <li>Increment the scores of all agents that made the minority choice
     *  <li>Tell all agents to update their local information given the
     *      last minority choice and choice history
     *  <li>Add the most recent minority choice outcome to the choice history
     * </ul>
     */
    public void stepForward() {
        // tell all agents to make a choice for this time step
        agents.makeChoices(
            choiceHistory.asList(agentMemorySize)
        );

        // retrieve the minority choice
        Choice minorityChoice = getLastMinorityChoice();

        // update based on the minority choice by incrementing agent scores,
        // telling agents to update and updating the choice history
        agents.incrementScoresForChoice(minorityChoice);
        agents.updateForChoice(
            minorityChoice,
            choiceHistory.asList(agentMemorySize)
        );
        choiceHistory.add(minorityChoice);
    }
}
