package cart.ui;

import cart.application.CouponService;
import cart.domain.Member;
import cart.dto.CouponResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponApiController {

    private final CouponService couponService;

    public CouponApiController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping
    public ResponseEntity<List<CouponResponse>> showCoupon(Member member) {
        return ResponseEntity.ok().body(couponService.findByMember(member));
    }
}
