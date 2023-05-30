package cart.domain.member;

import cart.domain.coupon.dto.CouponWithId;
import java.time.LocalDateTime;

public class MemberCoupon {

    private final CouponWithId coupon;
    private final LocalDateTime issuedDate;
    private final LocalDateTime expiredDate;

    public MemberCoupon(final CouponWithId coupon, final LocalDateTime issuedDate, final LocalDateTime expiredDate) {
        this.coupon = coupon;
        this.issuedDate = issuedDate;
        this.expiredDate = expiredDate;
    }

    public CouponWithId getCoupon() {
        return coupon;
    }

    public LocalDateTime getIssuedDate() {
        return issuedDate;
    }

    public LocalDateTime getExpiredDate() {
        return expiredDate;
    }
}
