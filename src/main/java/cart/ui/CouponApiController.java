package cart.ui;

import cart.application.CouponService;
import cart.domain.Member;
import cart.dto.response.ActiveCouponResponse;
import cart.dto.response.CouponDiscountResponse;
import cart.dto.response.CouponResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupons")
public class CouponApiController {

    private final CouponService couponService;

    public CouponApiController(final CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping
    public ResponseEntity<List<CouponResponse>> showCoupons(final Member member) {
        final List<CouponResponse> couponResponses = couponService.findAllByMember(member);
        return ResponseEntity.ok(couponResponses);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> publishCoupon(final Member member, @PathVariable final Long id) {
        couponService.publish(member, id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<ActiveCouponResponse>> showActiveCoupons(final Member member, @RequestParam int total) {
        final List<ActiveCouponResponse> activeCoupons = couponService.findActiveCoupons(member, total);
        return ResponseEntity.ok(activeCoupons);
    }

    @GetMapping("/{id}/discount")
    public ResponseEntity<CouponDiscountResponse> showCouponDiscountAmount(@PathVariable final Long id, @RequestParam int total) {
        final CouponDiscountResponse response = couponService.findCouponDiscountAmount(id, total);
        return ResponseEntity.ok(response);
    }
}
