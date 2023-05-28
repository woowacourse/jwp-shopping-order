package cart.ui;

import cart.application.MemberService;
import cart.application.dto.MemberResponse;
import cart.application.dto.MemberSaveRequest;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody @Valid final MemberSaveRequest memberSaveRequest) {
        final long savedMemberId = memberService.save(memberSaveRequest);
        return ResponseEntity.created(URI.create("/users/" + savedMemberId)).build();
    }

    @GetMapping("/{id}")
    public MemberResponse getMember(@PathVariable Long id) {
        return memberService.getById(id);
    }

    @GetMapping
    public List<MemberResponse> getMembers() {
        return memberService.getMembers();
    }

}
