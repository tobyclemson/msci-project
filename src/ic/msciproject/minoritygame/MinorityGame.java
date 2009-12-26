package ic.msciproject.minoritygame;

import java.util.Map;
import java.util.List;

/**
 * The MinorityGame class represents a minority game which can step forward in
 * time by performing actions on the associated agents and choice history.
 * @author tobyclemson
 */
public class MinorityGame {

    /**
     * An AgentManager instance containing AbstractAgent instances representing
     * the agents associated with the minority game. This is returned by the
     * {@link #getAgentManager} method.
     */
    protected AgentManager agentManager;

    /**
     * A ChoiceHistory instance holding a list of choices representing the
     * entire history of recent outcomes in the minority game. This is returned
     * by the {@link #getChoiceHistory} method.
     */
    protected ChoiceHistory choiceHistory;

    /**
     * Constructs an MinorityGame instance setting the agent manager
     * and choice history attributes to the supplied AgentManager and
     * ChoiceHistory instances.
     * @param agentManager An AgentManager instance containing the agents
     * associated with this minority game instance.
     * @param choiceHistory A ChoiceHistory instance to use as the history
     * of outcomes for this minority game instance.
     */
    public MinorityGame(
        AgentManager agentManager,
        ChoiceHistory choiceHistory
    ){
        this.agentManager = agentManager;
        this.choiceHistory = choiceHistory;
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
     * Returns the size of the current minority group.
     * @return The size of the minority group.
     */
    public int getMinoritySize() {
        // declare variables
        Map<Choice, Integer> choiceTotals;
        Integer numberChoosingA, numberChoosingB;
        
        // get a mapping of the choices Choice.A and Choice.B to the number of
        // agents that have make that choice.
        choiceTotals = agentManager.getChoiceTotals();
        
        // retrieve the numbers of agents making each choice.
        numberChoosingA = choiceTotals.get(Choice.A);
        numberChoosingB = choiceTotals.get(Choice.B);

        // return the smallest of those numbers.
        return Math.min(numberChoosingA, numberChoosingB);
    }

    /**
     * Returns the current minority choice.
     * @return The minority choice.
     */
    public Choice getMinorityChoice() {
        // declare variables
        Map<Choice, Integer> choiceTotals;
        Integer numberChoosingA, numberChoosingB;

        // get a mapping of the choices Choice.A and Choice.B to the number of
        // agents that have made that choice.
        choiceTotals = agentManager.getChoiceTotals();

        // retrieve the numbers of agents making each choice.
        numberChoosingA = choiceTotals.get(Choice.A);
        numberChoosingB = choiceTotals.get(Choice.B);

        // return the choice corresponding to the smallest of thise numbers.
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
     *  <li>Ask all agents to make a choice for this time step
     *  <li>Increment the scores of all agents that made the minority choice
     *  <li>Ask all agents to update their local information given the
     *      current minority choice
     *  <li>Add the most recent minority choice outcome to the choice history
     * </ul>
     */
    public void stepForward() {
        // tell all agentManager to make a choice for this time step
        agentManager.makeChoices();

        // retrieve the minority choice
        Choice minorityChoice = getMinorityChoice();

        // update based on the minority choice by incrementing agent scores,
        // telling agentManager to update and updating the choice history
        agentManager.incrementScoresForChoice(minorityChoice);
        agentManager.updateForChoice(minorityChoice);
        choiceHistory.add(minorityChoice);
    }

}
