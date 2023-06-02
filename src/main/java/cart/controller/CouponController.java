package cart.controller;

import cart.domain.Member;
import cart.service.CouponService;
import cart.service.response.CouponResponse;
import cart.service.response.DiscountPriceResponse;
import cart.service.response.MemberCouponResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(final CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping
    public ResponseEntity<List<MemberCouponResponse>> findMemberCoupons(final Member member) {
        final List<MemberCouponResponse> memberCoupons = couponService.findMemberCoupons(member);
        return ResponseEntity.ok(memberCoupons);
    }

    @GetMapping("/discount")
    public ResponseEntity<DiscountPriceResponse> discountPriceCoupon(final Member member,
                                                                     @RequestParam(value = "origin-price") final Integer originPrice,
                                                                     @RequestParam(value = "coupon-id") final Long memberCouponId) {
        final DiscountPriceResponse discount = couponService.discount(member, originPrice, memberCouponId);
        return ResponseEntity.ok(discount);
    }

    @PostMapping("/{id}/issue")
    public ResponseEntity<Void> issueCoupon(final Member member, @PathVariable final Long id) {
        //couponService.issue(member, id);
        return null;
    }
}
