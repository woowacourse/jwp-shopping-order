package shop.application.coupon;

import shop.application.member.dto.MemberCouponDto;
import shop.domain.coupon.CouponType;
import shop.domain.member.Member;

import java.util.List;

public interface CouponService {
    Long issueCoupon(Long memberId, CouponType couponType);

    List<MemberCouponDto> getAllCouponsOfMember(Member member);
}
