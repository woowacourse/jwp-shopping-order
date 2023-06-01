package cart.ui.coupon;

import cart.application.service.coupon.CouponReadService;
import cart.ui.MemberAuth;
import cart.ui.coupon.dto.CouponResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponReadController {

    private final CouponReadService couponReadService;

    public CouponReadController(final CouponReadService couponReadService) {
        this.couponReadService = couponReadService;
    }

    @GetMapping
    public ResponseEntity<List<CouponResponse>> findCoupons(final MemberAuth memberAuth) {
        final List<CouponResponse> couponResponses = couponReadService.findByMember(memberAuth);
        return ResponseEntity.ok(couponResponses);
    }
}
