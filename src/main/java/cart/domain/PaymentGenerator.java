package cart.domain;

import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class PaymentGenerator {
    private final DiscountStrategy discountStrategy;

    public PaymentGenerator(final DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    public Payment generate(Price price) {
        return discountStrategy.calculate(price);
    }

    public Map<Price, Price> getDiscountStandard() {
        return discountStrategy.getStandards();
    }
}
