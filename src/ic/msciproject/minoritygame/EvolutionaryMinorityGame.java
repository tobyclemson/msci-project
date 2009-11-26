package ic.msciproject.minoritygame;

import java.util.ArrayList;

/**
 * The EvolutionaryMinorityGame class represents the simplest variant of the
 * minority game in which only global information in the form of the history
 * string is available to the agents in making their choice of outcome. This
 * differs from the standard minority game in that agents are able to evolve
 * throughout the course of the game.
 * @author tobyclemson
 */
public class EvolutionaryMinorityGame extends AbstractMinorityGame {

    /**
     * Constructs an EvolutionaryMinorityGame instance setting the agents and
     * history string parameters to the supplied AgentCollection and
     * HistoryString instances.
     * @param agents An AgentCollection instance containing the agents
     * associated with this minority game instance.
     * @param historyString A HistoryString instance to use as the history
     * string for this minority game instance.
     */
    public EvolutionaryMinorityGame(
        AgentCollection agents,
        HistoryString historyString
    ) {
        super(agents, historyString);
    }

}
