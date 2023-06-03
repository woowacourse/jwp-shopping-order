package cart.ui;

import cart.application.MemberService;
import cart.auth.Auth;
import cart.domain.Member;
import cart.ui.dto.MemberGradeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/member")
    public ResponseEntity<MemberGradeResponse> getMemberGrade(@Auth final Member member) {
        return ResponseEntity.ok(memberService.getMemberInfo(member.getId()));
    }
}
