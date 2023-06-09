package cart.domain.disount;

import cart.domain.value.Price;

public interface DiscountPolicy {

    Price discount(Price price);

    int getValue();

    String getName();
}

