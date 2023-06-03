package cart.persistence.mapper;

import static cart.persistence.mapper.CouponMapper.convertCoupon;

import cart.domain.coupon.CouponWithId;
import cart.domain.member.MemberCoupon;
import cart.persistence.dao.dto.MemberCouponDto;
import java.util.List;
import java.util.stream.Collectors;

public class MemberCouponMapper {

    public static MemberCoupon convertMemberCoupon(final MemberCouponDto memberCouponDto) {
        final CouponWithId coupon = new CouponWithId(memberCouponDto.getCouponId(),
            convertCoupon(memberCouponDto));
        return new MemberCoupon(coupon, memberCouponDto.getIssuedAt(), memberCouponDto.getExpiredAt(),
            memberCouponDto.isUsed());
    }

    public static List<MemberCoupon> convertMemberCoupons(final List<MemberCouponDto> myCouponsByName) {
        return myCouponsByName.stream()
            .map(MemberCouponMapper::convertMemberCoupon)
            .collect(Collectors.toUnmodifiableList());
    }
}
