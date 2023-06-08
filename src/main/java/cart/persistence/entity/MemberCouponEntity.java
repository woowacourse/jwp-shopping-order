package cart.persistence.entity;

import java.time.LocalDateTime;

public class MemberCouponEntity {

    private final Long memberId;
    private final Long couponId;
    private final LocalDateTime issuedAt;
    private final LocalDateTime expiredAt;
    private final boolean isUsed;

    public MemberCouponEntity(final Long memberId, final Long couponId, final LocalDateTime issuedAt,
                              final LocalDateTime expiredAt,
                              final boolean isUsed) {
        this.memberId = memberId;
        this.couponId = couponId;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
        this.isUsed = isUsed;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getCouponId() {
        return couponId;
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
