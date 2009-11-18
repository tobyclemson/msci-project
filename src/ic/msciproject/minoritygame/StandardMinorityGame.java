package ic.msciproject.minoritygame;

import java.util.ArrayList;

public class StandardMinorityGame extends AbstractMinorityGame{

    public StandardMinorityGame(){
        super();
    }

    public StandardMinorityGame(
        ArrayList<AbstractAgent> agents,
        HistoryString historyString
    ){
        super(agents, historyString);
    }

}
