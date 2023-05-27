package cart.domain.discount;

import org.springframework.stereotype.Component;

@Component
public class BasicDiscountPolicy implements DiscountPolicy {

    private static final int FIRST_BOUND = 50000;
    private static final int FIRST_BOUND_DISCOUNT_AMOUNT = 5000;
    private static final int SECOND_BOUND = 30000;
    private static final int SECOND_BOUND_DISCOUNT_AMOUNT = 3000;

    @Override
    public Long calculate(final Long price) {
        if (price >= FIRST_BOUND) {
            return price - FIRST_BOUND_DISCOUNT_AMOUNT;
        }
        if (price >= SECOND_BOUND) {
            return price - SECOND_BOUND_DISCOUNT_AMOUNT;
        }
        return price;
    }
}
