package cart.domain.coupon;

import org.springframework.stereotype.Component;

@Component("rate")
public class RateStrategy implements Strategy {
    @Override
    public int calculate(final int price, final int amount) {
        return (int) Math.ceil(price * (amount / 100.0));
    }

    @Override
    public StrategyName getStrategyName() {
        return StrategyName.rate;
    }
}
