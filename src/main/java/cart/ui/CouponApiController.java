package cart.ui;

import cart.application.dto.coupon.CreateCouponRequest;
import cart.application.service.CouponService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupons")
public class CouponApiController {

    private final CouponService couponService;

    public CouponApiController(final CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping
    public ResponseEntity<Void> createCoupon(@RequestBody CreateCouponRequest request) {
        long id = couponService.createCoupon(request);
        return ResponseEntity.created(URI.create("/coupons/" + id)).build();
    }
}
