package cart.domain.coupon;

import org.springframework.stereotype.Component;

@Component("price")
public class PriceStrategy implements Strategy {
    @Override
    public int calculate(final int price, final int amount) {
        return (int) Math.ceil(amount);
    }

    @Override
    public StrategyName getStrategyName() {
        return StrategyName.price;
    }
}
