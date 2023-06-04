package cart.application;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.domain.Amount;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.dto.AvailableCouponResponse;
import cart.dto.CouponResponse;
import cart.dto.DiscountAmountResponse;
import cart.dto.SaveCouponRequest;
import cart.exception.BusinessException;
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

  public DiscountAmountResponse calculateDiscountAmount(final Member member, final Long couponId, final int totalAmount) {
    checkCouponOwner(member, couponId);
    final Coupon coupon = couponDao.findById(couponId)
        .orElseThrow(() -> new BusinessException("찾는 쿠폰이 존재하지 않습니다"));

    final Amount discountedAmount = coupon.apply(new Amount(totalAmount));

    return new DiscountAmountResponse(discountedAmount.getValue(), totalAmount - discountedAmount.getValue());
  }

  private void checkCouponOwner(Member member, Long couponId) {
    if (!memberCouponDao.existsByMemberIdAndCouponId(member.getId(), couponId)) {
      throw new BusinessException("해당 쿠폰을 가지고 있지 않습니다.");
    }
  }
}
