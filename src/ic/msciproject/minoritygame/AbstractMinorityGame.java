package ic.msciproject.minoritygame;

import java.util.ArrayList;

public class AbstractMinorityGame {

    private ArrayList<AbstractAgent> agents;
    private HistoryString historyString;

    public AbstractMinorityGame(){
        agents = new ArrayList<AbstractAgent>();
        historyString = new HistoryString();
    }

    public AbstractMinorityGame(
        ArrayList<AbstractAgent> agents,
        HistoryString historyString
    ){
        this.agents = agents;
        this.historyString = historyString;
    }

    public ArrayList<AbstractAgent> getAgents(){
        return agents;
    }

    public void setAgents(ArrayList<AbstractAgent> agents){
        this.agents = agents;
    }

    public HistoryString getHistoryString(){
        return historyString;
    }

    public void setHistoryString(HistoryString historyString) {
        this.historyString = historyString;
    }
}
