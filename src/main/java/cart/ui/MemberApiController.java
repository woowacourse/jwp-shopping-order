package cart.ui;

import cart.application.MemberService;
import cart.application.dto.response.PointResponse;
import cart.domain.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/points")
    public ResponseEntity<PointResponse> showPoint(final Member member) {
        return ResponseEntity.ok().body(memberService.findPointByMember(member));
    }
}