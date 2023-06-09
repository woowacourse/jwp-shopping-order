package cart.coupon.controller;

import cart.coupon.application.CouponQueryService;
import cart.coupon.application.dto.CouponResponse;
import cart.member.domain.Member;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CouponApiController {

  private final CouponQueryService couponQueryService;

  public CouponApiController(final CouponQueryService couponQueryService) {
    this.couponQueryService = couponQueryService;
  }

  @GetMapping("/coupons")
  public List<CouponResponse> showCoupons(final Member member) {
    return couponQueryService.searchCoupons(member);
  }
}
