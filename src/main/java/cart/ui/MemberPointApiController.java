package cart.ui;

import cart.application.MemberService;
import cart.domain.Member;
import cart.dto.response.MemberPointQueryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberPointApiController {

    private final MemberService memberService;

    public MemberPointApiController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/points")
    public ResponseEntity<MemberPointQueryResponse> getPoints(final Member member) {
        return ResponseEntity.ok().body(memberService.findPointsOf(member));
    }
}
