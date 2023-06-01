package cart.persistence.dto;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.persistence.entity.CouponEntity;
import cart.persistence.entity.MemberCouponEntity;
import java.time.LocalDateTime;

public class MemberCouponDetailDTO {

    private final MemberCouponEntity memberCouponEntity;
    private final CouponEntity couponEntity;

    public MemberCouponDetailDTO(final MemberCouponEntity memberCouponEntity, final CouponEntity couponEntity) {
        this.memberCouponEntity = memberCouponEntity;
        this.couponEntity = couponEntity;
    }

    public MemberCoupon toDomain() {
        Coupon coupon = couponEntity.toDomain();
        return new MemberCoupon(memberCouponEntity.getId(), coupon,
                false, memberCouponEntity.getExpiredAt().toLocalDateTime(), LocalDateTime.now());
    }

    public MemberCouponEntity getMemberCouponEntity() {
        return memberCouponEntity;
    }

    public CouponEntity getCouponEntity() {
        return couponEntity;
    }
}
