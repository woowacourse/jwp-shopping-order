package cart.domain.discount;

import org.springframework.stereotype.Component;

@Component
public class AdvancedDiscountPolicy implements DiscountPolicy {

    private static final long NO_DISCOUNT = 0L;
    private static final double DISCOUNT_RATE = 0.1;
    private static final int DISCOUNT_BOUND = 100_000;

    @Override
    public Long calculate(final Long price) {
        if (price >= DISCOUNT_BOUND) {
            return (long) (price * DISCOUNT_RATE);
        }
        return NO_DISCOUNT;
    }
}
