package cart.ui;

import cart.application.CouponService;
import cart.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(final CouponService couponService) {
        this.couponService = couponService;
    }

    @PatchMapping("/{couponId}")
    public ResponseEntity<Void> createCoupon(Member member, @PathVariable Long couponId) {
        couponService.addCoupon(member.getId(), couponId);
        return ResponseEntity.ok().build();
    }

}
