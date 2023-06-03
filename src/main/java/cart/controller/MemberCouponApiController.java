package cart.controller;

import cart.domain.Member;
import cart.dto.CouponResponse;
import cart.service.MemberCouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/coupons")
@RestController
public class MemberCouponApiController {

    private final MemberCouponService memberCouponService;

    public MemberCouponApiController(MemberCouponService memberCouponService) {
        this.memberCouponService = memberCouponService;
    }

    @GetMapping
    public ResponseEntity<CouponResponse> findAll(@AuthPrincipal Member member) {
        CouponResponse couponResponse = memberCouponService.findAll(member);
        return ResponseEntity.ok(couponResponse);
    }
}
