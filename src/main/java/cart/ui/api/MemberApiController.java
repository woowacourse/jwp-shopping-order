package cart.ui.api;

import cart.application.MemberService;
import cart.domain.member.Member;
import cart.dto.member.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<MemberResponse> getUserInfo(Member member) {
        MemberResponse memberResponse = memberService.getMemberInfo(member);
        return ResponseEntity.ok().body(memberResponse);
    }
}
