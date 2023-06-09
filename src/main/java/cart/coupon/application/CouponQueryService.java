package cart.coupon.application;

import cart.coupon.application.dto.CouponResponse;
import cart.coupon.dao.MemberCouponDao;
import cart.coupon.domain.Coupon;
import cart.coupon.domain.MemberCoupon;
import cart.coupon.domain.UsedStatus;
import cart.coupon.exception.CouponException;
import cart.coupon.exception.CouponExceptionType;
import cart.member.domain.Member;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouponQueryService {

  private final MemberCouponDao memberCouponDao;

  public CouponQueryService(final MemberCouponDao memberCouponDao) {
    this.memberCouponDao = memberCouponDao;
  }

  public List<CouponResponse> searchCoupons(final Member member) {

    final List<Coupon> coupons = searchCouponsOwnedByMember(member);

    return coupons.stream()
        .map(coupon -> new CouponResponse(
            coupon.getId(),
            coupon.getName(),
            coupon.findDiscountPrice().getValue()))
        .collect(Collectors.toList());
  }

  public List<Coupon> searchCouponsOwnedByMember(final Member member) {
    final List<MemberCoupon> memberCoupons = memberCouponDao.findByMemberId(
        member.getId(),
        UsedStatus.UNUSED.getValue()
    );

    return memberCoupons.stream()
        .map(MemberCoupon::getCoupon)
        .collect(Collectors.toList());
  }

  public Coupon searchCouponOwnedByMember(final Member member, final Long couponId) {
    final List<MemberCoupon> memberCoupons = memberCouponDao.findByMemberId(
        member.getId(),
        UsedStatus.UNUSED.getValue()
    );

    return memberCoupons.stream()
        .map(MemberCoupon::getCoupon)
        .filter(coupon -> coupon.isNotSame(couponId))
        .findAny()
        .orElseThrow(() -> new CouponException(CouponExceptionType.NOT_FOUND_MEMBER_COUPON));
  }
}
