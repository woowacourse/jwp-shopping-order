package cart.controller.member;

import cart.dto.member.MemberCreateRequest;
import cart.dto.member.MemberResponse;
import cart.service.member.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@Valid @RequestBody final MemberCreateRequest memberCreateRequest) {
        memberService.createMember(memberCreateRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> findAll() {
        return ResponseEntity.ok(memberService.findAll());
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> findById(@PathVariable final Long memberId) {
        return ResponseEntity.ok(memberService.findById(memberId));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteById(@PathVariable final Long memberId) {
        memberService.deleteById(memberId);
        return ResponseEntity.noContent().build();
    }
}
