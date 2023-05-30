package cart.domain.member;

import cart.domain.coupon.dto.CouponWithId;
import java.time.LocalDateTime;

public class MemberCoupon {

    private final CouponWithId coupon;
    private final LocalDateTime issuedDate;
    private final LocalDateTime expiredDate;
    private final boolean isUsed;

    public MemberCoupon(final CouponWithId coupon, final LocalDateTime issuedDate, final LocalDateTime expiredDate,
                        final boolean isUsed) {
        this.coupon = coupon;
        this.issuedDate = issuedDate;
        this.expiredDate = expiredDate;
        this.isUsed = isUsed;
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

    public boolean isUsed() {
        return isUsed;
    }
}
