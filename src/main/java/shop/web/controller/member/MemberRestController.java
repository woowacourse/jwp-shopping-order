package shop.web.controller.member;

import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import shop.application.coupon.CouponService;
import shop.application.member.MemberService;
import shop.application.member.dto.MemberCouponDto;
import shop.application.member.dto.MemberJoinDto;
import shop.application.member.dto.MemberLoginDto;
import shop.domain.member.Member;
import shop.web.controller.member.dto.MemberLoginResponse;

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
    public ResponseEntity<Void> joinMember(@RequestBody MemberJoinDto request) {
        memberService.join(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> loginMember(@RequestBody MemberLoginDto request) {
        String encryptedPassword = memberService.login(request);

        String token = request.getName() + ":" + encryptedPassword;
        String encodedToken = Base64Utils.encodeToUrlSafeString(token.getBytes());

        return ResponseEntity.ok(new MemberLoginResponse(encodedToken));
    }

    @GetMapping("/me/coupons")
    public ResponseEntity<List<MemberCouponDto>> getCouponsOfMember(Member member) {
        List<MemberCouponDto> responses = couponService.getAllCouponsOfMember(member).stream()
                .map(MemberCouponDto::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }
}
