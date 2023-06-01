package cart.controller.coupon;

import cart.dto.coupon.CouponCreateRequest;
import cart.dto.coupon.CouponResponse;
import cart.service.coupon.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RequestMapping("/coupons")
@RestController
public class CouponController {

    private final CouponService couponService;

    public CouponController(final CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping
    public ResponseEntity<Void> createCoupon(@Valid @RequestBody final CouponCreateRequest request) {
        long couponId = couponService.createCoupon(request);
        return ResponseEntity.created(URI.create("/coupons/" + couponId)).build();
    }

    @GetMapping
    public ResponseEntity<List<CouponResponse>> findAll() {
        return ResponseEntity.ok(couponService.findAllCoupons());
    }

    @GetMapping("/{couponId}")
    public ResponseEntity<CouponResponse> findById(@PathVariable final Long couponId) {
        return ResponseEntity.ok(couponService.findById(couponId));
    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<Void> deleteById(@PathVariable final Long couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{couponId}/members/{memberId}")
    public ResponseEntity<Void> giveCouponToMember(@PathVariable("couponId") final Long couponId,
                                                   @PathVariable("memberId") final Long memberId) {
        couponService.giveCouponToMember(couponId, memberId);
        return ResponseEntity.ok().build();
    }
}
