package cart.ui;

import cart.application.MemberService;
import cart.dto.request.MemberCreateRequest;
import cart.dto.response.MemberCreateResponse;
import cart.dto.response.MemberQueryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
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

    @PostMapping("/join")
    public ResponseEntity<MemberCreateResponse> join(@Valid @RequestBody MemberCreateRequest request) {
        MemberCreateResponse member = memberService.join(request);
        return ResponseEntity
                .created(URI.create("/members/" + member.getId()))
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberQueryResponse> findMemberById(@PathVariable Long id) {
        return ResponseEntity.ok().body(memberService.findMemberById(id));
    }
}
