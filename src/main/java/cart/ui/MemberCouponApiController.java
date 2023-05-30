package cart.ui;

import cart.application.MemberCouponService;
import cart.domain.Member;
import cart.domain.MemberCoupon;
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

    @PostMapping
    public ResponseEntity<Void> addMemberCoupon() {
        // 사용자 쿠폰 추가
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<MemberCoupon>> getMemberCoupons(Member member) {
        // 사용자가 갖는 쿠폰 정보 조회
        List<MemberCoupon> memberCoupons = memberCouponService.getMemberCoupons(member.getId());
        // TODO: memberCoupon 도메인 -> dto 로 변경
        return ResponseEntity.ok().body(memberCoupons);
    }
}
