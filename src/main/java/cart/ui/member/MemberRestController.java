package cart.ui.member;

import cart.application.CouponService;
import cart.application.MemberService;
import cart.domain.coupon.MemberCoupon;
import cart.domain.member.Member;
import cart.ui.member.dto.MemberCouponResponse;
import cart.ui.member.dto.request.MemberJoinRequest;
import cart.ui.member.dto.response.MemberLoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/users")
@RestController
public class MemberRestController {
    private final MemberService memberService;
    private final CouponService couponService;

    public MemberRestController(MemberService memberService, CouponService couponService) {
        this.memberService = memberService;
        this.couponService = couponService;
    }

    @PostMapping("/join")
    public ResponseEntity<Void> joinMember(@RequestBody MemberJoinRequest request) {
        Member member = new Member(request.getName(), request.getPassword());

        memberService.join(member);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> loginMember(@RequestBody MemberJoinRequest request) {
        Member member = new Member(request.getName(), request.getPassword());
        String encryptedPassword = memberService.login(member);

        String token = request.getName() + ":" + encryptedPassword;
        String encodedToken = Base64Utils.encodeToUrlSafeString(token.getBytes());

        return ResponseEntity.ok(new MemberLoginResponse(encodedToken));
    }

    @GetMapping("/me/coupons")
    public ResponseEntity<List<MemberCouponResponse>> getCouponsOfMember(Member member) {
        List<MemberCoupon> memberCoupons = couponService.getAllCouponsOfMember(member);

        return ResponseEntity.ok(
                memberCoupons.stream()
                        .map(MemberCouponResponse::of)
                        .collect(Collectors.toList())
        );
    }
}
