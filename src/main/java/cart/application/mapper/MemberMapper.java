package cart.application.mapper;

import cart.application.dto.member.MemberCouponResponse;
import cart.application.dto.member.MemberJoinRequest;
import cart.application.dto.member.MemberResponse;
import cart.domain.coupon.CouponWithId;
import cart.domain.member.EncryptedPassword;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupon;
import cart.domain.member.NaturalPassword;
import cart.domain.member.MemberWithId;
import cart.domain.security.SHA256Service;
import java.time.LocalDateTime;

public class MemberMapper {

    public static Member convertMember(final MemberJoinRequest memberJoinRequest) {
        final NaturalPassword naturalPassword = NaturalPassword.create(memberJoinRequest.getPassword());
        final String encodedPassword = SHA256Service.encrypt(naturalPassword.getPassword());
        return Member.create(memberJoinRequest.getName(), EncryptedPassword.create(encodedPassword));
    }

    public static MemberResponse convertMemberResponse(final Member member) {
        return new MemberResponse(member.name(), member.password());
    }

    public static MemberResponse convertMemberResponse(final MemberWithId memberWithId) {
        return new MemberResponse(memberWithId.getMemberId(), memberWithId.getMember().name(),
            memberWithId.getMember().password());
    }

    public static MemberCouponResponse convertMemberCouponResponse(final MemberCoupon memberCoupon) {
        final CouponWithId coupon = memberCoupon.getCoupon();
        return new MemberCouponResponse(
            coupon.getCouponId(), coupon.getCoupon().name(), coupon.getCoupon().discountRate(),
            coupon.getCoupon().expiredAt(), memberCoupon.isUsed());
    }

    public static MemberCoupon convertMemberCoupon(final CouponWithId coupon, final LocalDateTime issuedAt) {
        return new MemberCoupon(coupon, issuedAt, issuedAt.plusDays(coupon.getCoupon().period()), false);
    }
}
