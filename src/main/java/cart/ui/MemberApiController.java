package cart.ui;

import cart.application.MemberService;
import cart.domain.Member;
import cart.dto.PointResponse;
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

    @GetMapping("/point")
    public ResponseEntity<PointResponse> getMemberPoint(Member member) {
        PointResponse response = memberService.getMemberPoint(member);

        return ResponseEntity.ok(response);
    }
}
