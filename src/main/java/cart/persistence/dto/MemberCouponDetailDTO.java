package cart.persistence.dto;

import cart.domain.coupon.MemberCoupon;
import cart.persistence.entity.CouponEntity;
import cart.persistence.entity.MemberCouponEntity;
import cart.persistence.entity.MemberEntity;
import cart.persistence.mapper.MemberCouponMapper;

public class MemberCouponDetailDTO {

    private final MemberCouponEntity memberCouponEntity;
    private final CouponEntity couponEntity;
    private final MemberEntity memberEntity;

    public MemberCouponDetailDTO(final MemberCouponEntity memberCouponEntity, final CouponEntity couponEntity,
            final MemberEntity memberEntity) {
        this.memberCouponEntity = memberCouponEntity;
        this.couponEntity = couponEntity;
        this.memberEntity = memberEntity;
    }

    public MemberCoupon toDomain() {
        return MemberCouponMapper.toDomain(memberCouponEntity, couponEntity, memberEntity);
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
