package cart.application;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.domain.Amount;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.dto.AvailableCouponResponse;
import cart.dto.CouponResponse;
import cart.dto.SaveCouponRequest;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

  private final CouponDao couponDao;
  private final MemberCouponDao memberCouponDao;

  public CouponService(final CouponDao couponDao, MemberCouponDao memberCouponDao) {
    this.couponDao = couponDao;
    this.memberCouponDao = memberCouponDao;
  }

  public Long createCoupon(final SaveCouponRequest request) {
    return couponDao.createCoupon(
        new Coupon(request.getName(),
            new Amount(request.getDiscountAmount()),
            new Amount(request.getMinAmount())));
  }

  public List<CouponResponse> findMemberCouponsByMemberId(final Member member) {
    final List<MemberCoupon> memberCoupons = memberCouponDao.findMemberCouponsByMemberId(member.getId());

    return memberCoupons.stream()
        .map(memberCoupon -> {
          final Coupon coupon = memberCoupon.getCoupon();
          return new CouponResponse(coupon.getId(), coupon.getName(), coupon.getMinAmount().getValue(),
              coupon.getDiscountAmount().getValue(), memberCoupon.isUsed());
        }).collect(Collectors.toList());
  }

  public void issueCoupon(final Member member, final Long couponId) {
    memberCouponDao.save(member.getId(), couponId);
  }

  public List<AvailableCouponResponse> findAvailableCoupons(final Member member, final int totalAmount) {
    final List<MemberCoupon> availableMemberCoupons = memberCouponDao.findAvailableCouponsByMemberIdAndTotalAmount(
        member.getId(), totalAmount);

    return availableMemberCoupons.stream()
        .map(memberCoupon -> {
          final Coupon coupon = memberCoupon.getCoupon();
          return new AvailableCouponResponse(coupon.getId(), coupon.getName(), coupon.getMinAmount().getValue());
        }).collect(Collectors.toList());
  }
}
