package cart.persistence.entity;

import java.time.LocalDateTime;

public class MemberCouponEntity {
    private final Long id;
    private final Long memberId;
    private final Long couponId;
    private final LocalDateTime issuedAt;
    private final LocalDateTime expiredAt;
    private final Boolean isUsed;

    public MemberCouponEntity(Long id, Long memberId, Long couponId, LocalDateTime issuedAt,
                              LocalDateTime expiredAt, Boolean isUsed) {
        this.id = id;
        this.memberId = memberId;
        this.couponId = couponId;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
        this.isUsed = isUsed;
    }

    public MemberCouponEntity(Long memberId, Long couponId, LocalDateTime issuedAt,
                              LocalDateTime expiredAt, Boolean isUsed) {
        this(null, memberId, couponId, issuedAt, expiredAt, isUsed);
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

    public Boolean getIsUsed() {
        return isUsed;
    }
}
