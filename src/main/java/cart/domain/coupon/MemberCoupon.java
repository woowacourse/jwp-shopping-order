package cart.domain.coupon;

import cart.domain.cart.CartItems;
import java.time.LocalDateTime;

public class MemberCoupon {

    private final Long id;
    private final Coupon coupon;
    private final boolean isUsed;
    private final LocalDateTime expiredAt;
    private final LocalDateTime createdAt;

    public MemberCoupon(final Coupon coupon, final LocalDateTime expiredAt) {
        this(null, coupon, false, expiredAt, null);
    }

    public MemberCoupon(final Long id, final Coupon coupon, final boolean isUsed, final LocalDateTime expiredAt,
            final LocalDateTime createdAt) {
        this.id = id;
        this.coupon = coupon;
        this.isUsed = isUsed;
        this.expiredAt = expiredAt;
        this.createdAt = createdAt;
    }

    public boolean isApplicable(final CartItems cartItems) {
        if (isUsed || !checkValidPeriod()) {
            return false;
        }
        return coupon.isApplicable(cartItems);
    }

    private boolean checkValidPeriod() {
        return expiredAt.isAfter(LocalDateTime.now());
    }

    public int getDiscountPrice(final CartItems cartItems) {
        return coupon.getDiscountPrice(cartItems);
    }

    public Long getId() {
        return id;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
