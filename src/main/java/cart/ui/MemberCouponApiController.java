package cart.ui;

import cart.application.MemberCouponService;
import cart.domain.Coupon;
import cart.domain.member.Member;
import cart.domain.MemberCoupon;
import cart.dto.MemberCouponResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
        List<MemberCoupon> memberCoupons = memberCouponService.getMemberCoupons(member.getId());
        List<MemberCouponResponse> memberCouponResponses = memberCoupons.stream().map(memberCoupon -> {
            Coupon coupon = memberCoupon.getCoupon();
            return new MemberCouponResponse(
                    coupon.getId(),
                    coupon.getName(),
                    coupon.getDiscountRate(),
                    memberCoupon.getExpiredDate(),
                    memberCoupon.isUsed()
            );
        }).collect(Collectors.toList());
        return ResponseEntity.ok().body(memberCouponResponses);
    }
}
