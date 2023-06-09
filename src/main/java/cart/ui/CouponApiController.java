package cart.ui;

import cart.application.CouponService;
import cart.domain.Member;
import cart.dto.request.CouponIssueRequest;
import cart.dto.request.CouponRequest;
import cart.dto.response.CouponResponse;
import cart.dto.response.CouponsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/coupons")
public class CouponApiController {

    private final CouponService couponService;

    public CouponApiController(final CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping
    public ResponseEntity<CouponResponse> makeCoupon(@Valid @RequestBody CouponRequest couponRequest) {
        CouponResponse response = couponService.makeCoupon(couponRequest);
        return ResponseEntity.created(URI.create("/coupon/" + response.getId()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<CouponsResponse> getAllCoupons() {
        CouponsResponse couponsResponse = couponService.getAllCoupons();
        return ResponseEntity.ok(couponsResponse);
    }

    @PostMapping("/me")
    public ResponseEntity<Void> issueCouponToMember(Member member, @Valid @RequestBody CouponIssueRequest couponIssueRequest) {
        Long issuedId = couponService.issueCouponToMember(member, couponIssueRequest);
        return ResponseEntity.created(URI.create("/member-coupon/" + issuedId)).build();
    }

    @GetMapping("/me")
    public ResponseEntity<CouponsResponse> getAllMemberCoupons(Member member) {
        CouponsResponse allUnusedCouponsResponse = couponService.getAllUnusedCoupons(member);
        return ResponseEntity.ok().body(allUnusedCouponsResponse);
    }
}
