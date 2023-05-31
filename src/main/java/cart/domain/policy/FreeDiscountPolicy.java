package cart.domain.policy;

import cart.domain.Price;

public class FreeDiscountPolicy implements DiscountPolicy {
    public static final String NAME = "free";

    @Override
    public Price discount(Price price) {
        return new Price(0);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getValue() {
        return 0;
    }
}
