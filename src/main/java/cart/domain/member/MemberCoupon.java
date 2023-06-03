package cart.domain.member;

import cart.domain.coupon.dto.CouponWithId;
import cart.exception.BadRequestException;
import cart.exception.ErrorCode;
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

    public void checkValid() {
        validateCouponExpired();
        validateAlreadyUsed();
    }

    private void validateCouponExpired() {
        final LocalDateTime now = LocalDateTime.now();
        if (expiredAt.isBefore(now)) {
            throw new BadRequestException(ErrorCode.COUPON_EXPIRED);
        }
    }

    private void validateAlreadyUsed() {
        if (isUsed()) {
            throw new BadRequestException(ErrorCode.COUPON_ALREADY_USED);
        }
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
