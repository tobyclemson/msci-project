package msci.mg.agents;

import msci.mg.ChoiceMemory;
import msci.mg.Strategy;
import msci.mg.StrategyManager;

import java.util.List;

public interface IntelligentAgent extends Agent {
    ChoiceMemory getMemory();
    List<Strategy> getStrategies();
    StrategyManager getStrategyManager();
}
