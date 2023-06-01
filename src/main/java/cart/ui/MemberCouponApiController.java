package cart.ui;

import cart.application.MemberCouponService;
import cart.domain.member.Member;
import cart.dto.coupon.MemberCouponResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/me/coupons")
public class MemberCouponApiController {

    private final MemberCouponService memberCouponService;

    public MemberCouponApiController(final MemberCouponService memberCouponService) {
        this.memberCouponService = memberCouponService;
    }

    @PostMapping("/{couponId}")
    public ResponseEntity<Void> addMemberCoupon(final Member member, @PathVariable final Long couponId) {
        memberCouponService.add(member.getId(), couponId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<MemberCouponResponse>> getMemberCoupons(final Member member) {
        return ResponseEntity.ok().body(memberCouponService.getMemberCoupons(member.getId()));
    }
}
