package cart.dao.entity;

import cart.domain.AmountCoupon;
import cart.domain.Coupon;
import cart.domain.CouponInfo;
import cart.domain.PercentageCoupon;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class MemberCouponEntity {
    private final Long id;
    private final Long memberId;
    private final Long couponId;
    private final LocalDateTime expiredAt;

    public MemberCouponEntity(final Long id, final Long memberId, final Long couponId, final LocalDateTime expiredAt) {
        this.id = id;
        this.memberId = memberId;
        this.couponId = couponId;
        this.expiredAt = expiredAt;
    }

    
    public Coupon toCoupon(final CouponEntity couponEntity) {
        CouponInfo couponInfo = new CouponInfo(
                id,
                couponEntity.getName(),
                couponEntity.getMinOrderPrice(),
                couponEntity.getMaxDiscountPrice(),
                expiredAt
        );
        // TODO: 5/30/23 type enum 고려
        if (couponEntity.getType().equals("퍼센트")) {
            return new PercentageCoupon(
                    couponInfo,
                    couponEntity.getDiscountPercentage()
            );
        }
        return new AmountCoupon(
                couponInfo,
                couponEntity.getDiscountAmount()
        );
    }

    // TODO: 5/31/23 메서드 책임 분리 
    public Coupon toCoupon(final Map<Long, CouponEntity> allCoupons) {
        CouponEntity couponEntity = allCoupons.get(couponId);
        CouponInfo couponInfo = new CouponInfo(
                id,
                couponEntity.getName(),
                couponEntity.getMinOrderPrice(),
                couponEntity.getMaxDiscountPrice(),
                expiredAt
        );
        // TODO: 5/30/23 type enum 고려
        if (couponEntity.getType().equals("퍼센트")) {
            return new PercentageCoupon(
                    couponInfo,
                    couponEntity.getDiscountPercentage()
            );
        }
        return new AmountCoupon(
                couponInfo,
                couponEntity.getDiscountAmount()
        );
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

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MemberCouponEntity that = (MemberCouponEntity) o;
        return Objects.equals(id, that.id)
                && Objects.equals(memberId, that.memberId)
                && Objects.equals(couponId, that.couponId)
                && Objects.equals(expiredAt, that.expiredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, memberId, couponId, expiredAt);
    }

    @Override
    public String toString() {
        return "MemberCouponEntity{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", couponId=" + couponId +
                ", expiredAt=" + expiredAt +
                '}';
    }
}
