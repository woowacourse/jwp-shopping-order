package cart.controller.api;

import cart.controller.AuthPrincipal;
import cart.domain.Member;
import cart.dto.MemberCouponsResponse;
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
    public ResponseEntity<MemberCouponsResponse> findAll(@AuthPrincipal Member member) {
        MemberCouponsResponse couponsResponse = couponService.findAllByMember(member);
        return ResponseEntity.ok(couponsResponse);
    }
}
