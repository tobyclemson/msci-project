package ic.msciproject.minoritygame;

import java.util.ArrayList;
import java.util.Properties;

public class AbstractMinorityGame {

    private int numberOfAgents;
    private ArrayList<AbstractAgent> agents;
    private String agentType;

    public AbstractMinorityGame(){
        numberOfAgents = 0;
        agents = new ArrayList<AbstractAgent>();
        agentType = "abstract";
    }

    public AbstractMinorityGame(Properties properties){
        numberOfAgents = Integer.parseInt(
            properties.getProperty("number-of-agents")
        );
        agentType = properties.getProperty("agent-type");

        agents = new ArrayList<AbstractAgent>(numberOfAgents);
        for(int i = 0; i < numberOfAgents; i++){
            if(agentType.equals("probabilistic")){
                agents.add(new ProbabilisticAgent());
            } else {
                agents.add(new AbstractAgent());
            }
        }
    }

    public ArrayList<AbstractAgent> getAgents(){
        return agents;
    }

    public int getNumberOfAgents(){
        return numberOfAgents;
    }
}
