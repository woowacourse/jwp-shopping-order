package cart.ui;

import cart.application.MemberService;
import cart.domain.Member;
import cart.dto.MemberResponse;
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
