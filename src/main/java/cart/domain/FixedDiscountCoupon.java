package cart.domain;

import cart.exception.CannotApplyCouponException;

public class FixedDiscountCoupon extends Coupon {

    private final Long id;
    private final Long member_id;
    private final int discountPrice;

    public FixedDiscountCoupon(long member_id, int discountPrice) {
        this(null, member_id, discountPrice);
    }

    public FixedDiscountCoupon(Long id, Long member_id, int discountPrice) {
        this.id = id;
        this.member_id = member_id;
        this.discountPrice = discountPrice;
    }

    @Override
    public Money apply(Money price) {
        try {
            return Money.from(discountPrice);
        } catch (Exception e) {
            throw new CannotApplyCouponException();
        }
    }

    @Override
    public Money getDiscountPrice() {
        return Money.from(this.discountPrice);
    }
}
