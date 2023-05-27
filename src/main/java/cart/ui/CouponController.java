package cart.ui;

import cart.application.CouponService;
import cart.domain.Member;
import cart.dto.CouponTypeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(final CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/{couponTypeId}")
    public ResponseEntity<Void> createCoupon(Member member, @PathVariable Long couponTypeId) {
        Long couponId = couponService.createCoupon(member.getId(), couponTypeId);
        return ResponseEntity.created(URI.create("/coupons/" + couponId)).build();
    }

    @PatchMapping("/{couponId}")
    public ResponseEntity<Void> issuanceCoupon(Member member, @PathVariable Long couponId) {
        couponService.addCoupon(member.getId(), couponId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CouponTypeResponse>> showAllCouponsTypes() {
        List<CouponTypeResponse> couponsType = couponService.getCouponsType();
        return ResponseEntity.ok().body(couponsType);
    }

}
