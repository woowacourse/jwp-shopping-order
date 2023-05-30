package cart.ui;

import cart.application.CouponService;
import cart.domain.Member;
import cart.dto.CouponRequest;
import cart.dto.CouponResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CouponController {
    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/users/coupons")
    public ResponseEntity<Void> issueCoupon(final Member member, @RequestBody CouponRequest couponRequest) {
        couponService.issueCoupon(member, couponRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<CouponResponse>> showAllCoupon(final Member member) {
        return ResponseEntity.ok(couponService.showAllCoupons(member));
    }

    @GetMapping("/users/coupons")
    public ResponseEntity<List<CouponResponse>> showMembersCoupons(final Member member) {
        return ResponseEntity.ok(couponService.showMembersCoupons(member));
    }
}
