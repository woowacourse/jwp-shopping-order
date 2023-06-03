package cart.ui;

import cart.application.dto.coupon.CreateCouponRequest;
import cart.application.dto.coupon.FindCouponsResponse;
import cart.application.dto.coupon.IssueCouponRequest;
import cart.application.service.CouponService;
import cart.application.service.MemberCouponService;
import cart.domain.Member;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupons")
public class CouponApiController {

    private final CouponService couponService;
    private final MemberCouponService memberCouponService;

    public CouponApiController(final CouponService couponService,
            final MemberCouponService memberCouponService) {
        this.couponService = couponService;
        this.memberCouponService = memberCouponService;
    }

    @PostMapping
    public ResponseEntity<Void> createCoupon(@RequestBody CreateCouponRequest request) {
        long id = couponService.createCoupon(request);
        return ResponseEntity.created(URI.create("/coupons/" + id)).build();
    }

    @PostMapping("/{couponId}")
    public ResponseEntity<Void> issueCoupon(Member member, @PathVariable Long couponId,
            @RequestBody IssueCouponRequest request) {
        long id = memberCouponService.createMemberCoupon(member, couponId, request);
        return ResponseEntity.created(URI.create("/member-coupons/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<FindCouponsResponse> findAllCoupons() {
        return ResponseEntity.ok(couponService.findAll());
    }
}
