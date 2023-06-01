package cart.persistence.entity;

import java.sql.Timestamp;

public class MemberCouponEntity {

    private final Long id;
    private final long memberId;
    private final long couponId;
    private final boolean isUsed;
    private final Timestamp expiredAt;
    private final Timestamp createdAt;

    public MemberCouponEntity(final long memberId, final long couponId, final boolean isUsed, final Timestamp expiredAt,
            final Timestamp createdAt) {
        this(null, memberId, couponId, isUsed, expiredAt, createdAt);
    }

    public MemberCouponEntity(final Long id, final long memberId, final long couponId, final boolean isUsed,
            final Timestamp expiredAt,
            final Timestamp createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.couponId = couponId;
        this.isUsed = isUsed;
        this.expiredAt = expiredAt;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public long getCouponId() {
        return couponId;
    }

    public boolean getIsUsed() {
        return isUsed;
    }

    public Timestamp getExpiredAt() {
        return expiredAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
