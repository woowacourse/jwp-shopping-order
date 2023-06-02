package cart.domain.coupon;

import java.util.Objects;

public class DiscountAmount {

    private final int discountAmount;

    private DiscountAmount(final int discountAmount) {
        if (discountAmount <= 0) {
            throw new IllegalStateException("할인 금액은 0보다 작을 수 없습니다.");
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
