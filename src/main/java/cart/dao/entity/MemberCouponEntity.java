package cart.dao.entity;

import cart.domain.*;

import java.time.LocalDateTime;
import java.util.Objects;

public class MemberCouponEntity {
    private final Long id;
    private final Long memberId;
    private final Long couponId;
    private final Boolean isUsed;
    private final LocalDateTime expiredAt;
    private final LocalDateTime createdAt;

    public MemberCouponEntity(final Long id, final Long memberId, final Long couponId, final Boolean isUsed, final LocalDateTime expiredAt, final LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.couponId = couponId;
        this.isUsed = isUsed;
        this.expiredAt = expiredAt;
        this.createdAt = createdAt;
    }

    public static MemberCouponEntity from(final MemberCoupon memberCoupon) {
        Boolean isUsed = memberCoupon instanceof UsedMemberCoupon;
        return new MemberCouponEntity(
                memberCoupon.getId(),
                memberCoupon.getMember().getId(),
                memberCoupon.getCoupon().getCouponInfo().getId(),
                isUsed,
                memberCoupon.getExpiredAt(),
                memberCoupon.getCreatedAt()
        );
    }

    public MemberCoupon toMemberCoupon(final Coupon coupon, final Member member) {
        System.out.println(isUsed);
        if (isUsed) {
            return new UsedMemberCoupon(id, coupon, member, expiredAt, createdAt);
        }
        return new UsableMemberCoupon(id, coupon, member, expiredAt, createdAt);
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

    public Boolean getUsed() {
        return isUsed;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MemberCouponEntity that = (MemberCouponEntity) o;
        return Objects.equals(id, that.id)
                && Objects.equals(memberId, that.memberId)
                && Objects.equals(couponId, that.couponId)
                && Objects.equals(isUsed, that.isUsed)
                && Objects.equals(expiredAt, that.expiredAt)
                && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, memberId, couponId, isUsed, expiredAt, createdAt);
    }

    @Override
    public String toString() {
        return "MemberCouponEntity{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", couponId=" + couponId +
                ", isUsed=" + isUsed +
                ", expiredAt=" + expiredAt +
                ", createdAt=" + createdAt +
                '}';
    }
}
