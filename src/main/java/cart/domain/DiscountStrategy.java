package cart.domain;

import java.util.Map;

public interface DiscountStrategy {
    Payment calculate(Price price);

    Map<Price, Price> getStandards();
}
