package ic.msciproject.minoritygame;

/**
 * The StandardMinorityGame class represents the simplest variant of the
 * minority game in which only global information in the form of the choice 
 * history is available to the agentManager in making their choice of outcome.
 * @author tobyclemson
 */
public class StandardMinorityGame extends AbstractMinorityGame{

    /**
     * Constructs a StandardMinorityGame instance setting the agent manager,
     * choice history and agent memory size attributes to the supplied
     * AgentManager, ChoiceHistory and integer instances.
     * @param agentManager An AgentManager instance containing the agents
     * associated with this minority game instance.
     * @param choiceHistory A ChoiceHistory instance to use as the history of
     * outcomes for this minority game instance.
     * @param agentMemorySize The number of past minority choices each agent
     * can remember.
     */
    public StandardMinorityGame(
        AgentManager agentManager,
        ChoiceHistory choiceHistory,
        int agentMemorySize
    ){
        super(agentManager, choiceHistory, agentMemorySize);
    }

    /**
     * Takes a step forward in the game.
     *
     * The step is taken as follows:
     * <ul>
     *  <li>Ask all agents to make a choice for this time step
     *  <li>Increment the scores of all agents that made the minority choice
     *  <li>Ask all agents to update their local information given the
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
