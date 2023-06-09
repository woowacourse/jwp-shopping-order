package shop.web.controller.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.application.coupon.CouponService;
import shop.application.member.MemberService;
import shop.application.member.dto.MemberCouponDto;
import shop.application.member.dto.MemberJoinDto;
import shop.application.member.dto.MemberLoginDto;
import shop.domain.member.Member;
import shop.web.controller.member.dto.request.MemberJoinRequest;
import shop.web.controller.member.dto.request.MemberLoginRequest;
import shop.web.controller.member.dto.response.MemberCouponResponse;
import shop.web.controller.member.dto.response.MemberLoginResponse;

import java.util.List;

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
        MemberJoinDto memberJoinDto = toMemberJoinDto(request);
        memberService.join(memberJoinDto);

        return ResponseEntity.ok().build();
    }

    private MemberJoinDto toMemberJoinDto(MemberJoinRequest request) {
        return new MemberJoinDto(request.getName(), request.getPassword());
    }

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> loginMember(@RequestBody MemberLoginRequest request) {
        MemberLoginDto memberLoginDto = toMemberLoginDto(request);

        String loginToken = memberService.login(memberLoginDto);

        return ResponseEntity.ok(new MemberLoginResponse(loginToken));
    }

    private MemberLoginDto toMemberLoginDto(MemberLoginRequest request) {
        return new MemberLoginDto(request.getName(), request.getPassword());
    }

    @GetMapping("/me/coupons")
    public ResponseEntity<List<MemberCouponResponse>> getCouponsOfMember(Member member) {
        List<MemberCouponDto> memberCouponDtos = couponService.getAllCouponsOfMember(member);
        List<MemberCouponResponse> responses = MemberCouponResponse.of(memberCouponDtos);

        return ResponseEntity.ok(responses);
    }
}
