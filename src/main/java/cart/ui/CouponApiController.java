package cart.ui;

import cart.application.CouponService;
import cart.domain.Member;
import cart.dto.AvailableCouponResponse;
import cart.dto.CouponResponse;
import cart.dto.DiscountAmountResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupons")
public class CouponApiController {

  private final CouponService couponService;

  public CouponApiController(CouponService couponService) {
    this.couponService = couponService;
  }

  @GetMapping
  public ResponseEntity<List<CouponResponse>> findMemberCoupons(final Member member) {
    return ResponseEntity.ok(couponService.findMemberCouponsByMemberId(member));
  }

  @PostMapping("/{id}")
  public ResponseEntity<Void> issueCoupon(final Member member, @PathVariable Long id) {
    couponService.issueCoupon(member, id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/active")
  public ResponseEntity<List<AvailableCouponResponse>> findAvailableCoupons(final Member member, final int total) {
    return ResponseEntity.ok(couponService.findAvailableCoupons(member, total));
  }

  @GetMapping("/{id}/discount")
  public ResponseEntity<DiscountAmountResponse> calculateDiscountAmount(final Member member,
      @PathVariable Long id, final int total) {
    return ResponseEntity.ok(couponService.calculateDiscountAmount(member, id, total));
  }
}
