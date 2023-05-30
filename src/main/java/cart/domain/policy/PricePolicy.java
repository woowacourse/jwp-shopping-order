package cart.domain.policy;

import cart.domain.Price;

public class PricePolicy implements DiscountPolicy {
    public static final String NAME = "price";
    private final Price price;

    public PricePolicy(int value) {
        this.price = new Price(value);
    }

    @Override
    public Price discount(Price price) {
        if (this.price.isMoreThan(price)) {
            return new Price(0);
        }
        return price.minus(this.price);
    }

    @Override
    public int getValue() {
        return price.getValue();
    }

    @Override
    public String getName() {
        return NAME;
    }
}
