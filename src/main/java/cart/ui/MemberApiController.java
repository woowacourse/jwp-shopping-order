package cart.ui;

import cart.application.MemberService;
import cart.domain.Member;
import cart.dto.WithdrawRequest;
import cart.dto.WithdrawResponse;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<WithdrawResponse> getWithdrawPoint(final Member member,
                                                             @Valid @RequestBody final WithdrawRequest request) {
        final Long point = request.getPoint();
        final Long totalCash = memberService.withdrawPoint(member, point);

        return ResponseEntity.ok(WithdrawResponse.from(totalCash));
    }
}
