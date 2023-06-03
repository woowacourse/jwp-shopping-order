package cart.controller;

import cart.controller.response.CouponResponseDto;
import cart.domain.Member;
import cart.service.CouponService;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    private final CouponService couponService;

    public CouponController(final CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/issue")
    public ResponseEntity<List<CouponResponseDto>> issueCoupon(Member member) {
        return ResponseEntity.created(URI.create("ignored"))
                .body(couponService.issue(member));
    }

    @GetMapping
    public ResponseEntity<List<CouponResponseDto>> getAllCoupon(Member member) {
        return ResponseEntity.ok()
                .body(couponService.findAllByMember(member));
    }

}
