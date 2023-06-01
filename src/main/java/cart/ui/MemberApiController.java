package cart.ui;

import cart.application.MemberService;
import cart.domain.Member;
import cart.dto.request.MemberCreateRequest;
import cart.dto.response.PointResponse;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/members")
@RestController
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> signUp(@Valid @RequestBody MemberCreateRequest request) {
        memberService.signUp(request);
        return ResponseEntity.created(URI.create("/cart-items")).build();
    }

    @GetMapping("/points")
    public PointResponse showMemberPoint(Member member) {
        return PointResponse.of(member);
    }
}
