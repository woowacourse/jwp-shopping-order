package cart.domain.policy;

import cart.domain.Price;

public class PriceDiscountPolicy implements DiscountPolicy {
    public static final String NAME = "price";
    private final Price price;

    public PriceDiscountPolicy(int value) {
        Price price = new Price(value);
        validateIsZero(price);
        this.price = price;
    }

    private void validateIsZero(Price price) {
        if (price.isZero()) {
            throw new IllegalArgumentException("할인가격은 0원이 될 수 없습니다.");
        }
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
