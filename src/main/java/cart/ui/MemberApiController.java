package cart.ui;

import cart.application.MemberService;
import cart.domain.Member;
import cart.dto.DepositRequest;
import cart.dto.DepositResponse;
import cart.dto.PointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/members")
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/cash")
    public ResponseEntity<DepositResponse> postDepositPoint(final Member member,
                                                            @Valid @RequestBody final DepositRequest request) {
        final Long point = request.getPoint();
        final Long totalCash = memberService.depositPoint(member, point);

        return ResponseEntity.ok(DepositResponse.from(totalCash));
    }

    @GetMapping("/cash")
    public ResponseEntity<PointResponse> getTotalPoint(final Member member) {
        final Long totalCash = memberService.findPoint(member);

        return ResponseEntity.ok(PointResponse.from(totalCash));
    }
}
