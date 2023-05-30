package cart.ui.controller;

import cart.application.MemberService;
import cart.domain.member.Member;
import cart.ui.controller.dto.response.MemberPointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/members")
@RestController
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/point")
    public ResponseEntity<MemberPointResponse> getMemberPoint(Member member) {
        MemberPointResponse response = memberService.getMemberPoint(member);
        return ResponseEntity.ok(response);
    }
}
