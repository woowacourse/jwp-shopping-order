package cart.controller;

import cart.dto.CouponsResponse;
import cart.dto.MemberInfo;
import cart.service.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/coupons")
@RestController
public class MemberCouponApiController {

    private final CouponService couponService;

    public MemberCouponApiController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping
    public ResponseEntity<CouponsResponse> findAll(@AuthPrincipal MemberInfo member) {
        CouponsResponse couponResponse = couponService.findAllByMember(member);
        return ResponseEntity.ok(couponResponse);
    }
}
