package ic.msciproject.minoritygame;

import java.util.ArrayList;

public class EvolutionaryMinorityGame extends AbstractMinorityGame {

    public EvolutionaryMinorityGame(){
        super();
    }

    public EvolutionaryMinorityGame(
        ArrayList<AbstractAgent> agents,
        HistoryString historyString
    ) {
        super(agents, historyString);
    }

}
