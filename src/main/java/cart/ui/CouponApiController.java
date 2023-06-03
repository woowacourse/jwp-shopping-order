package cart.ui;

import cart.application.CouponService;
import cart.domain.Member;
import cart.dto.CouponResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
  public ResponseEntity<List<CouponResponse>> showCartItems(Member member) {
    return ResponseEntity.ok(couponService.findMemberCouponsByMemberId(member));
  }
}
