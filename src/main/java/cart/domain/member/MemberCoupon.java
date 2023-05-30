package cart.domain.member;

import cart.domain.coupon.dto.CouponWithId;
import java.time.LocalDateTime;

public class MemberCoupon {

    private final CouponWithId coupon;
    private final LocalDateTime issuedAt;
    private final LocalDateTime expiredAt;
    private final boolean isUsed;

    public MemberCoupon(final CouponWithId coupon, final LocalDateTime issuedAt, final LocalDateTime expiredAt,
                        final boolean isUsed) {
        this.coupon = coupon;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
        this.isUsed = isUsed;
    }

    public CouponWithId getCoupon() {
        return coupon;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public boolean isUsed() {
        return isUsed;
    }
}
