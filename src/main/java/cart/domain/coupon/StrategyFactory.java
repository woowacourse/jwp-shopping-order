package cart.domain.coupon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class StrategyFactory {

    private Map<StrategyName, Strategy> strategies;

    @Autowired
    public StrategyFactory(Set<Strategy> strategySet) {
        createStrategy(strategySet);
    }

    public Strategy findStrategy(String strategyName) {
        return strategies.get(StrategyName.valueOf(strategyName));
    }

    private void createStrategy(Set<Strategy> strategySet) {
        strategies = new HashMap<StrategyName, Strategy>();
        strategySet.forEach(
                strategy -> strategies.put(strategy.getStrategyName(), strategy));
    }
}
