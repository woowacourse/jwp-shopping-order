package cart.controller;

import cart.domain.Member;
import cart.service.CouponService;
import cart.service.response.CouponResponse;
import cart.service.response.DiscountPriceResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    private final CouponService couponService;

    public CouponController(final CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping
    public ResponseEntity<List<CouponResponse>> findMemberCoupons(final Member member) {
        final List<CouponResponse> memberCoupons = couponService.findMemberCoupons(member);
        return ResponseEntity.ok(memberCoupons);
    }

    @GetMapping("/discount")
    public ResponseEntity<DiscountPriceResponse> discountPriceCoupon(final Member member,
                                                                     @RequestParam(value = "total-price") final Integer originPrice,
                                                                     @RequestParam(value = "coupon-id") final Long memberCouponId) {
        final DiscountPriceResponse discount = couponService.discount(member, originPrice, memberCouponId);
        return ResponseEntity.ok(discount);
    }
}
