package cart.domain;

import org.springframework.stereotype.Component;

@Component
public class DeliveryFeeCalculatorImpl implements DeliveryFeeCalculator {

    private static final int DEFAULT_DELIVERY_FEE = 3_000;

    @Override
    public Money calculate(Order order) {
        return Money.from(DEFAULT_DELIVERY_FEE);
    }
}
