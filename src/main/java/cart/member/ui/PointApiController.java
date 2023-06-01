package cart.member.ui;

import cart.member.application.MemberService;
import cart.member.domain.Member;
import cart.member.dto.PointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PointApiController {
    private final MemberService memberService;
    
    public PointApiController(final MemberService memberService) {
        this.memberService = memberService;
    }
    
    @GetMapping("/point")
    public ResponseEntity<PointResponse> getCurrentPoint(Member member) {
        final Long currentPoint = memberService.getCurrentPoint(member);
        return ResponseEntity.ok(new PointResponse(currentPoint));
    }
}
