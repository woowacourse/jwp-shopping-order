package cart.domain.discount_strategy;

import cart.domain.Price;

public interface DiscountCalculator {

    Price calculate(final Price price);
}
