package ic.msciproject.minoritygame;

/**
 * The StandardMinorityGame class represents the simplest variant of the
 * minority game in which only global information in the form of the choice 
 * history is available to the agents in making their choice of outcome.
 * @author tobyclemson
 */
public class StandardMinorityGame extends AbstractMinorityGame{

    /**
     * Constructs a StandardMinorityGame instance setting the agents, choice
     * history and agent memory size attributes to the supplied AgentManager,
     * ChoiceHistory and integer instances.
     * @param agents An AgentManager instance containing the agents associated
     * with this minority game instance.
     * @param choiceHistory A ChoiceHistory instance to use as the history of
     * outcomes for this minority game instance.
     * @param agentMemorySize The number of past minority choices each agent
     * can remember.
     */
    public StandardMinorityGame(
        AgentManager agents,
        ChoiceHistory choiceHistory,
        int agentMemorySize
    ){
        super(agents, choiceHistory, agentMemorySize);
    }

}
