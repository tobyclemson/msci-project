package msci.mg.agents;

import java.util.List;
import msci.mg.Agent;
import msci.mg.ChoiceMemory;
import msci.mg.Strategy;
import msci.mg.StrategyManager;

/**
 * The {@code IntelligentAgent} interface represents an {@code Agent} that
 * utilises local or global information in making a choice.
 * 
 * @author Toby Clemson
 */
public interface IntelligentAgent extends Agent {

    ChoiceMemory getMemory();

    List<Strategy> getStrategies();

    StrategyManager getStrategyManager();

}
