package cart.domain.coupon;

import java.util.Objects;

public class DiscountAmount {

    private static final int MINIMUM_AMOUNT = 0;

    private final int discountAmount;

    private DiscountAmount(final int discountAmount) {
        if (discountAmount <= MINIMUM_AMOUNT) {
            throw new IllegalStateException(String.format("할인 금액은 %s보다 작을 수 없습니다.", MINIMUM_AMOUNT));
        }

        this.discountAmount = discountAmount;
    }

    public static DiscountAmount from(final int discountAmount) {
        return new DiscountAmount(discountAmount);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final DiscountAmount that = (DiscountAmount) o;
        return discountAmount == that.discountAmount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(discountAmount);
    }

    public int getDiscountAmount() {
        return discountAmount;
    }
}
