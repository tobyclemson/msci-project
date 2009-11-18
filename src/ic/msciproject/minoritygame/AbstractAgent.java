package ic.msciproject.minoritygame;

public class AbstractAgent {
    private StrategyCollection strategies;

    public AbstractAgent() {
        strategies = new StrategyCollection();
    }

    public void setStrategies(StrategyCollection strategies) {
        this.strategies = strategies;
    }

    public StrategyCollection getStrategies() {
        return strategies;
    }
}
