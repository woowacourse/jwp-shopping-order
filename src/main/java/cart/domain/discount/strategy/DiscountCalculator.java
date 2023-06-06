package cart.domain.discount.strategy;

import cart.domain.Price;

public interface DiscountCalculator {

    Price calculate(final Price price);
}
