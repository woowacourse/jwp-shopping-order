package cart.db.mapper;

import cart.db.entity.MemberCouponDetailEntity;
import cart.db.entity.MemberCouponEntity;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.domain.member.Member;

import java.util.List;
import java.util.stream.Collectors;

public class MemberCouponMapper {

    public static MemberCoupon toDomain(final MemberCouponDetailEntity entity) {
        return new MemberCoupon(
                entity.getId(),
                new Member(entity.getMemberId(), entity.getMemberName(), entity.getMemberPassword()),
                new Coupon(entity.getCouponId(), entity.getCouponName(), entity.getDiscountRate(), entity.getPeriod(), entity.getExpiredAt()),
                entity.getUsed(),
                entity.getIssuedAtToMember(),
                entity.getExpiredAtToMember()
        );
    }

    public static List<MemberCoupon> toDomain(final List<MemberCouponDetailEntity> entities) {
        return entities.stream()
                .map(MemberCouponMapper::toDomain)
                .collect(Collectors.toList());
    }

    public static MemberCouponEntity toEntity(final MemberCoupon memberCoupon) {
        return new MemberCouponEntity(
                memberCoupon.getId(),
                memberCoupon.getMember().getId(),
                memberCoupon.getCoupon().getId(),
                memberCoupon.getIssuedAt(),
                memberCoupon.getExpiredAt(),
                memberCoupon.isUsed()
        );
    }
}
