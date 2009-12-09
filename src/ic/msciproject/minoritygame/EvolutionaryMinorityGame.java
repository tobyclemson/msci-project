package ic.msciproject.minoritygame;

/**
 * The EvolutionaryMinorityGame class represents the simplest variant of the
 * minority game in which only global information in the form of the choice
 * history is available to the agents in making their choice of outcome. This
 * differs from the standard minority game in that agents are able to evolve
 * throughout the course of the game.
 * @author tobyclemson
 */
public class EvolutionaryMinorityGame extends AbstractMinorityGame {

    /**
     * Constructs an EvolutionaryMinorityGame instance setting the agents, 
     * choice history and agent memory size parameters to the supplied
     * AgentManager, ChoiceHistory and integer instances.
     * @param agentManager An AgentManager instance containing the agents
     * associated with this minority game instance.
     * @param choiceHistory A ChoiceHistory instance to use as the choice
     * history for this minority game instance.
     * @param agentMemorySize The number of past minority choices each agent
     * can remember
     */
    public EvolutionaryMinorityGame(
        AgentManager agentManager,
        ChoiceHistory choiceHistory,
        int agentMemorySize
    ) {
        super(agentManager, choiceHistory, agentMemorySize);
    }

}
