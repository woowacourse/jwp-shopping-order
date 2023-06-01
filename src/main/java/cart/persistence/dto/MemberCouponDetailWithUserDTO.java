package cart.persistence.dto;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.persistence.entity.CouponEntity;
import cart.persistence.entity.MemberCouponEntity;
import cart.persistence.entity.MemberEntity;
import java.time.LocalDateTime;

public class MemberCouponDetailWithUserDTO {

    private final MemberCouponEntity memberCouponEntity;
    private final CouponEntity couponEntity;
    private final MemberEntity memberEntity;

    public MemberCouponDetailWithUserDTO(final MemberCouponEntity memberCouponEntity, final CouponEntity couponEntity,
            final MemberEntity memberEntity) {
        this.memberCouponEntity = memberCouponEntity;
        this.couponEntity = couponEntity;
        this.memberEntity = memberEntity;
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

    public MemberEntity getMemberEntity() {
        return memberEntity;
    }
}
