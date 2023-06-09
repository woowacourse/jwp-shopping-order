package cart.controller;

import cart.controller.response.CouponResponseDto;
import cart.controller.response.DiscountResponseDto;
import cart.domain.Member;
import cart.service.CouponService;
import java.util.List;
import org.springframework.http.HttpStatus;
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

    @PostMapping("{coupon-id}/issue")
    public ResponseEntity<CouponResponseDto> issueCoupon(Member member, @PathVariable("coupon-id") Long couponId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(couponService.issue(member, couponId));
    }

    @GetMapping
    public ResponseEntity<List<CouponResponseDto>> getAllCoupon(Member member) {
        return ResponseEntity.ok()
                .body(couponService.findAllByMember(member));
    }

    @GetMapping("/discount")
    public ResponseEntity<DiscountResponseDto> getDiscountPrice(Member member, @RequestParam("origin-price") Integer originPrice, @RequestParam("member-coupon-id") Long memberCouponId) {
        return ResponseEntity.ok()
                .body(couponService.calculateDiscountPrice(member, originPrice, memberCouponId));
    }

}