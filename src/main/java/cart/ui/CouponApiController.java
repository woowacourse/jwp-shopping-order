package cart.ui;

import cart.application.CouponService;
import cart.dto.CouponsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CouponApiController {

    private final CouponService couponService;

    public CouponApiController(final CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping("/coupons")
    public ResponseEntity<CouponsResponse> getAllCoupons() {
        CouponsResponse couponsResponse = couponService.getAllCoupons();
        return ResponseEntity.ok(couponsResponse);
    }
}
