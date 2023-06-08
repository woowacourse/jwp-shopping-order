package cart.member.presentation;

import cart.member.presentation.request.MemberAddRequest;
import cart.member.application.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberApiController {
    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> addMember(@RequestBody MemberAddRequest memberAddRequest) {
        memberService.addMember(memberAddRequest.toDto());
        return ResponseEntity.ok().build();
    }
}
