package cart.application.dto.mapper;

import cart.application.dto.member.MemberCouponResponse;
import cart.application.dto.member.MemberResponse;
import cart.application.dto.member.MemberSaveRequest;
import cart.domain.coupon.dto.CouponWithId;
import cart.domain.member.EncryptedPassword;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupon;
import cart.domain.member.NaturalPassword;
import cart.domain.member.dto.MemberWithId;
import cart.domain.security.SHA256Service;

public class MemberMapper {

    public static Member convertMember(final MemberSaveRequest memberSaveRequest) {
        final NaturalPassword naturalPassword = NaturalPassword.create(memberSaveRequest.getPassword());
        final String encodedPassword = SHA256Service.encrypt(naturalPassword.getPassword());
        return Member.create(memberSaveRequest.getName(), EncryptedPassword.create(encodedPassword));
    }

    public static MemberResponse convverMemberResponse(final Member member) {
        return new MemberResponse(member.name(), member.password());
    }

    public static MemberResponse convertMemberResponse(final MemberWithId memberWithId) {
        return new MemberResponse(memberWithId.getId(), memberWithId.getMember().name(),
            memberWithId.getMember().password());
    }

    public static MemberCouponResponse convertMemberCouponResponse(final MemberCoupon memberCoupon) {
        final CouponWithId coupon = memberCoupon.getCoupon();
        return new MemberCouponResponse(
            coupon.getId(), coupon.getCoupon().name(), coupon.getCoupon().discountRate(),
            coupon.getCoupon().expiredAt(), memberCoupon.isUsed());
    }
}
