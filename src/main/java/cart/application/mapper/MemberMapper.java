package cart.application.mapper;

import cart.application.dto.member.MemberCouponResponse;
import cart.application.dto.member.MemberJoinRequest;
import cart.application.dto.member.MemberResponse;
import cart.domain.coupon.Coupon;
import cart.domain.member.EncryptedPassword;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupon;
import cart.domain.member.NaturalPassword;
import cart.domain.security.SHA256Service;
import java.time.LocalDateTime;

public class MemberMapper {

    public static Member convertMember(final MemberJoinRequest memberJoinRequest) {
        final NaturalPassword naturalPassword = NaturalPassword.create(memberJoinRequest.getPassword());
        final String encodedPassword = SHA256Service.encrypt(naturalPassword.getPassword());
        return Member.create(memberJoinRequest.getName(), EncryptedPassword.create(encodedPassword));
    }

    public static MemberResponse convertMemberResponse(final Member Member) {
        return new MemberResponse(Member.memberId(), Member.name(), Member.password());
    }

    public static MemberCouponResponse convertMemberCouponResponse(final MemberCoupon memberCoupon) {
        final Coupon coupon = memberCoupon.getCoupon();
        return new MemberCouponResponse(
            coupon.couponId(), coupon.name(), coupon.discountRate(),
            coupon.expiredAt(), memberCoupon.isUsed());
    }

    public static MemberCoupon convertMemberCoupon(final Coupon coupon, final LocalDateTime issuedAt) {
        return new MemberCoupon(coupon, issuedAt, issuedAt.plusDays(coupon.period()), false);
    }
}
