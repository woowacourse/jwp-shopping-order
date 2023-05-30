package cart.domain.policy;

import cart.domain.Price;

public interface DiscountPolicy {

    Price discount(Price price);

    int getValue();

    String getName();
}

