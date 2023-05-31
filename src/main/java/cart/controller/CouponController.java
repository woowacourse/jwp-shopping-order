package cart.controller;

import cart.config.auth.Auth;
import cart.domain.Member;
import cart.dto.CouponResponse;
import cart.service.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping
    public ResponseEntity<List<CouponResponse>> getCouponsByMember(@Auth Member member) {
        List<CouponResponse> coupons = couponService.findByMember(member);
        return ResponseEntity.ok().body(coupons);
    }
}
