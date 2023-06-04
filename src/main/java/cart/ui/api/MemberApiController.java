package cart.ui.api;

import cart.application.MemberService;
import cart.domain.member.Member;
import cart.dto.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<MemberResponse> findMember(Member member) {
        Member findMember = memberService.findMember(member);
        MemberResponse memberResponse = new MemberResponse(
                findMember.getId(),
                findMember.getEmail(),
                findMember.getMoney(),
                findMember.getPoint()
        );
        return ResponseEntity.ok(memberResponse);
    }
}
