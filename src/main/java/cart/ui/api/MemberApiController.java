package cart.ui.api;

import cart.application.service.MemberService;
import cart.ui.dto.response.MemberResponse;
import java.util.List;
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

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllMember(){
        final List<MemberResponse> memberResponses = memberService.getAll();
        return ResponseEntity.ok(memberResponses);
    }
}
