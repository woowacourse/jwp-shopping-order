package cart.persistence.mapper;

import cart.domain.coupon.dto.CouponWithId;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupon;
import cart.domain.member.dto.MemberWithId;
import cart.persistence.dao.dto.CartItemDto;
import cart.persistence.dao.dto.MemberCouponDto;
import cart.persistence.entity.MemberEntity;
import java.util.List;
import java.util.stream.Collectors;

public class MemberMapper {

    public static Member convertMember(final MemberEntity memberEntity) {
        return Member.createWithEncodedPassword(memberEntity.getName(), memberEntity.getPassword());
    }

    public static MemberWithId convertMemberRes(final MemberEntity memberEntity) {
        return new MemberWithId(memberEntity.getId(), convertMember(memberEntity));
    }

    public static Member convertMember(final CartItemDto cartItemDto) {
        return Member.create(cartItemDto.getMemberName(), cartItemDto.getMemberPassword());
    }

    public static List<MemberCoupon> convertMemberCoupons(final List<MemberCouponDto> myCouponsByName) {
        return myCouponsByName.stream()
            .map(memberCouponDto -> {
                final CouponWithId coupon = new CouponWithId(memberCouponDto.getCouponId(),
                    memberCouponDto.getCouponName(), memberCouponDto.getDiscountRate(),
                    memberCouponDto.getCouponPeriod(), memberCouponDto.getExpiredDate());
                return new MemberCoupon(coupon, memberCouponDto.getIssuedDate(), memberCouponDto.getExpiredDate(),
                    memberCouponDto.isUsed());
            }).collect(Collectors.toUnmodifiableList());
    }
}
