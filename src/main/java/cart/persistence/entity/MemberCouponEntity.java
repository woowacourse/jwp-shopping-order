package cart.persistence.entity;

import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
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

    public static MemberCouponEntity from(final MemberCoupon memberCoupon) {
        Coupon coupon = memberCoupon.getCoupon();
        Member member = memberCoupon.getMember();
        return new MemberCouponEntity(
                memberCoupon.getId(),
                member.getId(),
                coupon.getId(),
                memberCoupon.isUsed(),
                Timestamp.valueOf(memberCoupon.getExpiredAt()),
                Timestamp.valueOf(memberCoupon.getCreatedAt())
        );
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
