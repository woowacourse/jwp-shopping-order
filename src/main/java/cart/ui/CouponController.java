package cart.ui;

import cart.application.CouponService;
import cart.domain.Coupon;
import cart.dto.CouponRequest;
import cart.dto.CouponResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CouponController {

    private final CouponService couponService;

    public CouponController(final CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<CouponResponse>> showAllCoupons() {
        List<Coupon> coupons = couponService.findAll();
        List<CouponResponse> couponResponses = coupons.stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(couponResponses);
    }

    @GetMapping("/coupon/{id}")
    public ResponseEntity<CouponResponse> showCoupons(@PathVariable final Long id) {
        Coupon coupon = couponService.findById(id);

        return ResponseEntity.ok(CouponResponse.from(coupon));
    }

    @PostMapping("/coupon")
    public ResponseEntity<Void> addCoupon(@RequestBody CouponRequest request) {
        Long couponId = couponService.create(request);
        return ResponseEntity.created(URI.create("/coupon/" + couponId)).build();
    }

    @DeleteMapping("/coupon/{id}")
    public ResponseEntity<Void> deleteCoupons(@PathVariable final Long id) {
        couponService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
