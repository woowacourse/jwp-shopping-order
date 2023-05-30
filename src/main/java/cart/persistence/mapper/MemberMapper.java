package cart.persistence.mapper;

import static cart.persistence.mapper.CouponMapper.convertCoupon;

import cart.domain.coupon.dto.CouponWithId;
import cart.domain.member.EncryptedPassword;
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
        return Member.create(memberEntity.getName(), EncryptedPassword.create(memberEntity.getPassword()));
    }

    public static MemberWithId convertMemberWithId(final MemberEntity memberEntity) {
        return new MemberWithId(memberEntity.getId(), convertMember(memberEntity));
    }

    public static Member convertMember(final CartItemDto cartItemDto) {
        return Member.create(cartItemDto.getMemberName(), EncryptedPassword.create(cartItemDto.getMemberPassword()));
    }

    public static Member convertMember(final List<MemberCouponDto> myCouponsByName, final MemberEntity memberEntity) {
        return Member.create(memberEntity.getName(), EncryptedPassword.create(memberEntity.getPassword()),
            convertMemberCoupons(myCouponsByName));
    }

    public static List<MemberCoupon> convertMemberCoupons(final List<MemberCouponDto> myCouponsByName) {
        return myCouponsByName.stream()
            .map(memberCouponDto -> {
                final CouponWithId coupon = new CouponWithId(memberCouponDto.getCouponId(),
                    convertCoupon(memberCouponDto));
                return new MemberCoupon(coupon, memberCouponDto.getIssuedAt(), memberCouponDto.getExpiredAt(),
                    memberCouponDto.isUsed());
            }).collect(Collectors.toUnmodifiableList());
    }
}
