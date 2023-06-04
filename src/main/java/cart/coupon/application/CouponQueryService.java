package cart.coupon.application;

import cart.coupon.application.dto.CouponResponse;
import cart.coupon.dao.CouponDao;
import cart.coupon.domain.Coupon;
import cart.member.domain.Member;
import cart.member_coupon.application.MemberCouponQueryService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouponQueryService {

  private final CouponDao couponDao;
  private final MemberCouponQueryService memberCouponQueryService;

  public CouponQueryService(final CouponDao couponDao,
      final MemberCouponQueryService memberCouponQueryService) {
    this.couponDao = couponDao;
    this.memberCouponQueryService = memberCouponQueryService;
  }

  public List<CouponResponse> searchCoupons(final Member member) {

    final List<Coupon> coupons = memberCouponQueryService.searchCouponsIdOwenByMember(member);

    return coupons.stream()
        .map(coupon -> new CouponResponse(
            coupon.getId(),
            coupon.getName(),
            coupon.findDiscountPrice().getValue()))
        .collect(Collectors.toList());
  }
}
