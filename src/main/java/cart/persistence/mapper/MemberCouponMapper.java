package cart.persistence.mapper;

import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.persistence.entity.CouponEntity;
import cart.persistence.entity.MemberCouponEntity;
import cart.persistence.entity.MemberEntity;
import java.time.LocalDateTime;

public class MemberCouponMapper {

    private MemberCouponMapper() {
    }

    public static MemberCoupon toDomain(final MemberCouponEntity memberCouponEntity, final CouponEntity couponEntity,
            final MemberEntity memberEntity) {
        if (memberCouponEntity == null) {
            return null;
        }
        Coupon coupon = couponEntity.toDomain();
        Member member = memberEntity.toDomain();
        return new MemberCoupon(memberCouponEntity.getId(), coupon, member, false,
                memberCouponEntity.getExpiredAt().toLocalDateTime(),
                LocalDateTime.now());
    }
}
