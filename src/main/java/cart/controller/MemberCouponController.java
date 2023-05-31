package cart.controller;

import cart.auth.Auth;
import cart.auth.Credential;
import cart.dto.MemberCouponDto;
import cart.service.MemberCouponService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/member-coupons")
@RestController
public class MemberCouponController {

    private final MemberCouponService memberCouponService;

    public MemberCouponController(final MemberCouponService memberCouponService) {
        this.memberCouponService = memberCouponService;
    }

    @GetMapping
    public ResponseEntity<List<MemberCouponDto>> findAll(@Auth final Credential credential) {
        final List<MemberCouponDto> couponDtos = memberCouponService.findByMemberId(credential.getMemberId());
        return ResponseEntity.ok(couponDtos);
    }
}
