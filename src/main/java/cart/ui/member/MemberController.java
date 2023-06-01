package cart.ui.member;

import cart.application.member.MemberService;
import cart.domain.member.Member;
import cart.dto.member.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/member")
    public ResponseEntity<MemberResponse> showMember(final Member member) {
        MemberResponse response = memberService.findByEmail(member);
        return ResponseEntity.ok(response);
    }
}
