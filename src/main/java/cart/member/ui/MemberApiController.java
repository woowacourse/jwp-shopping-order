package cart.member.ui;

import cart.member.application.MemberService;
import cart.member.domain.Member;
import cart.member.ui.request.DepositRequest;
import cart.member.ui.response.DepositResponse;
import cart.member.ui.response.TotalCashResponse;
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
    public ResponseEntity<DepositResponse> postDepositCash(final Member member,
                                                           @Valid @RequestBody final DepositRequest request) {
        final Long cash = request.getCashToCharge();
        final Long totalCash = memberService.depositCash(member, cash);

        return ResponseEntity.ok(DepositResponse.from(totalCash));
    }

    @GetMapping("/cash")
    public ResponseEntity<TotalCashResponse> getTotalCash(final Member member) {
        final Long totalCash = memberService.findCash(member);

        return ResponseEntity.ok(TotalCashResponse.from(totalCash));
    }
}
