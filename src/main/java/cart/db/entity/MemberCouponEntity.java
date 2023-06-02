package cart.db.entity;

import java.time.LocalDateTime;

public class MemberCouponEntity {

    private final Long id;
    private final Long memberId;
    private final Long couponId;
    private final LocalDateTime issuedAt;
    private final LocalDateTime expiredAt;
    private final Boolean isUsed;

    public MemberCouponEntity(
            final Long id,
            final Long memberId,
            final Long couponId,
            final LocalDateTime issuedAt,
            final LocalDateTime expiredAt,
            final Boolean isUsed
    ) {
        this.id = id;
        this.memberId = memberId;
        this.couponId = couponId;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
        this.isUsed = isUsed;
    }

    public Long getId() {
        return id;
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

    public Boolean isUsed() {
        return isUsed;
    }
}
