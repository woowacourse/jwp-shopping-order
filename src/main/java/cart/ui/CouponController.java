package cart.ui;

import cart.application.CouponService;
import cart.domain.Member;
import cart.dto.request.CouponCreateRequest;
import cart.dto.response.CouponIssuableResponse;
import cart.dto.response.CouponResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/users/coupons")
    public ResponseEntity<Void> publishUserCoupon(Member member, @RequestBody CouponCreateRequest couponCreateRequest) {
        Long savedId = couponService.publishUserCoupon(member, couponCreateRequest);
        return ResponseEntity.created(URI.create("/coupons/" + savedId)).build();
    }

    @GetMapping("/users/coupons")
    public ResponseEntity<List<CouponResponse>> getUserCoupons(Member member) {
        List<CouponResponse> coupons = couponService.getUserCoupon(member);
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<CouponIssuableResponse>> getCoupons(Member member) {
        List<CouponIssuableResponse> coupons = couponService.getCoupons(member);
        return ResponseEntity.ok(coupons);
    }
}
