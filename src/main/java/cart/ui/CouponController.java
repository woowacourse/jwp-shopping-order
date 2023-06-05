package cart.ui;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import cart.application.CouponService;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.dto.MemberCouponResponse;

@RestController
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<MemberCouponResponse>> showMemberCoupons(Member member) {
        List<MemberCoupon> memberCoupons = couponService.getMemberCouponsOf(member);

        return ResponseEntity.ok(MemberCouponResponse.of(memberCoupons));
    }

    @PostMapping("/member/coupons/{couponId}")
    public ResponseEntity<Void> issueCouponTo(Member member, @PathVariable Long couponId) {
        couponService.issueCouponTo(member, couponId);

        return ResponseEntity.noContent().build();
    }
}
