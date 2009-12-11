package ic.msciproject.minoritygame;

/**
 * The StandardMinorityGame class represents the simplest variant of the
 * minority game in which only global information in the form of the choice 
 * history is available to the agentManager in making their choice of outcome.
 * @author tobyclemson
 */
public class StandardMinorityGame extends AbstractMinorityGame{

    /**
     * Constructs a StandardMinorityGame instance setting the agent manager and
     * choice history attributes to the supplied AgentManager and ChoiceHistory
     * instances.
     * @param agentManager An AgentManager instance containing the agents
     * associated with this minority game instance.
     * @param choiceHistory A ChoiceHistory instance to use as the history of
     * outcomes for this minority game instance.
     */
    public StandardMinorityGame(
        AgentManager agentManager,
        ChoiceHistory choiceHistory
    ){
        super(agentManager, choiceHistory);
    }

    /**
     * Takes a step forward in the game.
     *
     * The step is taken as follows:
     * <ul>
     *  <li>Ask all agents to make a choice for this time step
     *  <li>Increment the scores of all agents that made the minority choice
     *  <li>Ask all agents to update their local information given the
     *      last minority choice
     *  <li>Add the most recent minority choice outcome to the choice history
     * </ul>
     */
    public void stepForward() {
        // tell all agentManager to make a choice for this time step
        agentManager.makeChoices();

        // retrieve the minority choice
        Choice minorityChoice = getLastMinorityChoice();

        // update based on the minority choice by incrementing agent scores,
        // telling agentManager to update and updating the choice history
        agentManager.incrementScoresForChoice(minorityChoice);
        agentManager.updateForChoice(minorityChoice);
        choiceHistory.add(minorityChoice);
    }

}
