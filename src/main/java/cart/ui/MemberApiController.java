package cart.ui;

import cart.application.MemberService;
import cart.domain.member.Member;
import cart.domain.member.MemberPoint;
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
    public ResponseEntity<PointResponse> getPoint(Member member) {
        final MemberPoint point = memberService.getPoint(member);
        final PointResponse pointResponse = new PointResponse(point.getPoint());
        return ResponseEntity.ok(pointResponse);
    }
}
