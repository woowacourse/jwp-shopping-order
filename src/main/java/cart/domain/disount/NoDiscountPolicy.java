package cart.domain.disount;

import cart.domain.value.Price;

public class NoDiscountPolicy implements DiscountPolicy {
    public static final String NAME = "not";

    @Override
    public Price discount(Price price) {
        return price;
    }

    @Override
    public int getValue() {
        return 0;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
