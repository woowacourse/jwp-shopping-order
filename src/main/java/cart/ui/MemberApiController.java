package cart.ui;

import cart.application.MemberService;
import cart.domain.Member;
import cart.dto.MemberPointQueryResponse;
import cart.dto.MemberQueryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<MemberQueryResponse>> findAllMembers() {
        return ResponseEntity.ok().body(memberService.findAllMembers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberQueryResponse> findMemberById(@PathVariable Long id) {
        return ResponseEntity.ok().body(memberService.findMemberById(id));
    }

    @GetMapping("/points")
    public ResponseEntity<MemberPointQueryResponse> getPoints(final Member member) {
        return ResponseEntity.ok().body(memberService.findPointsOf(member));
    }
}
