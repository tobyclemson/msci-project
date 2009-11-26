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
     * An AgentCollection instance holding instances AbstractAgent used to
     * represent the agents associated with the minority game. This is returned
     * by the {@link #getAgents} method.
     */
    private AgentCollection agents;

    /**
     * A HistoryString instance holding the history string representing a
     * string of recent outcomes in the minority game. This is returned by the
     * {@link #getHistoryString} method.
     */
    private HistoryString historyString;

    /**
     * Constructs an AbstractMinorityGame instance setting the agents and
     * history string parameters to the supplied AgentCollection and
     * HistoryString instances.
     * @param agents An AgentCollection instance containing the agents
     * associated with this minority game instance.
     * @param historyString A HistoryString instance to use as the history
     * string for this minority game instance.
     */
    public AbstractMinorityGame(
        AgentCollection agents,
        HistoryString historyString
    ){
        this.agents = agents;
        this.historyString = historyString;
    }

    /**
     * Returns an AgentCollection of derivatives of AbstractAgent representing
     * the agents associated with this instance of the minority game.
     * @return An AgentCollection containing the agents associated with the
     * minority game.
     */
    public AgentCollection getAgents(){
        return agents;
    }

    /**
     * Returns a HistoryString instance containing a string of the recent
     * outcomes of time steps in the minority game
     * @return A HistoryString instance representing the history string for the
     * minority game.
     */
    public HistoryString getHistoryString(){
        return historyString;
    }

    /**
     * Returns the size of the minority group in the last step.
     * @return The size of the minority group.
     */
    public int getLastMinoritySize() {
        // declare variables
        Map<String, Integer> lastChoiceTotals;
        Integer numberChoosingZero, numberChoosingOne;
        
        // get a mapping of the choices '0' and '1' to the number of agents
        // making that choice in the last step.
        lastChoiceTotals = agents.getLastChoiceTotals();
        
        // retrieve the numbers of agents making each choice.
        numberChoosingZero = lastChoiceTotals.get("0");
        numberChoosingOne = lastChoiceTotals.get("1");

        // return the smallest of those numbers.
        return Math.min(numberChoosingOne, numberChoosingZero);
    }

    public String getLastMinorityChoice() {
        // declare variables
        Map<String, Integer> lastChoiceTotals;
        Integer numberChoosingZero, numberChoosingOne;

        // get a mapping of the choices '0' and '1' to the number of agents
        // making that choice in the last step.
        lastChoiceTotals = agents.getLastChoiceTotals();

        // retrieve the numbers of agents making each choice.
        numberChoosingZero = lastChoiceTotals.get("0");
        numberChoosingOne = lastChoiceTotals.get("1");

        // return the smallest of those numbers.
        if(numberChoosingZero < numberChoosingOne)
            return "0";
        else
            return "1";
    }

    /**
     * Takes a step forward in the game.
     *
     * The step is taken as follows:
     * <ul>
     *  <li>Tell all agents to make a choice for this time step
     *  <li>Increment
     * </ul>
     */
    public void stepForward() {
        // tell all agents to make a choice for this time step
        agents.makeChoices(historyString.toString());

        // retrieve the minority choice
        String minorityChoice = getLastMinorityChoice();

        // update based on the minority choice by incrementing agent scores,
        // telling agents to update and updating history string
        agents.incrementScoresForChoice(minorityChoice);
        agents.updateForChoice(minorityChoice, historyString.toString());
        historyString.push(minorityChoice);
    }
}
