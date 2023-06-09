package cart.domain.discount;

public class BasicDiscountPolicy implements DiscountPolicy {

    private static final Long FIRST_BOUND = 50000L;
    private static final Long FIRST_BOUND_DISCOUNT_AMOUNT = 5000L;
    private static final Long SECOND_BOUND = 30000L;
    private static final Long SECOND_BOUND_DISCOUNT_AMOUNT = 3000L;
    private static final long NO_DISCOUNT = 0L;

    @Override
    public Long calculate(final Long price) {
        if (price >= FIRST_BOUND) {
            return FIRST_BOUND_DISCOUNT_AMOUNT;
        }
        if (price >= SECOND_BOUND) {
            return SECOND_BOUND_DISCOUNT_AMOUNT;
        }
        return NO_DISCOUNT;
    }
}
