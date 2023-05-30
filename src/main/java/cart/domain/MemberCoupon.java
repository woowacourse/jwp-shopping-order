package cart.domain;

import java.time.LocalDateTime;

public class MemberCoupon {

    private final Coupon coupon;
    private final boolean isUsed;
    private final LocalDateTime issuedDate;
    private final LocalDateTime expiredDate;

    public MemberCoupon(final Coupon coupon, final boolean isUsed, final LocalDateTime issuedDate, final LocalDateTime expiredDate) {
        this.coupon = coupon;
        this.isUsed = isUsed;
        this.issuedDate = issuedDate;
        this.expiredDate = expiredDate;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public LocalDateTime getIssuedDate() {
        return issuedDate;
    }

    public LocalDateTime getExpiredDate() {
        return expiredDate;
    }
}
