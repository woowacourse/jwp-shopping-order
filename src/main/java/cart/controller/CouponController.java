package cart.controller;

import cart.dto.CouponSaveRequest;
import cart.service.CouponService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/coupons")
@RestController
public class CouponController {

    private final CouponService couponService;

    public CouponController(final CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody final CouponSaveRequest request) {
        final Long savedId = couponService.save(request);
        return ResponseEntity.created(URI.create("/coupons/" + savedId)).build();
    }
}
