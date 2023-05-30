package cart.ui;

import javax.validation.Valid;

import cart.application.MemberService;
import cart.config.AuthPrincipal;
import cart.dto.AuthMember;
import cart.dto.MemberCashChargeRequest;
import cart.dto.MemberCashChargeResponse;
import cart.dto.MemberShowCurrentCashResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/cash")
    public ResponseEntity<MemberCashChargeResponse> chargeCash(@AuthPrincipal AuthMember authMember,
                                     @Valid @RequestBody MemberCashChargeRequest memberCashChargeRequest) {
        MemberCashChargeResponse memberCashChargeResponse = memberService.chargeCash(authMember, memberCashChargeRequest);
        return ResponseEntity.ok(memberCashChargeResponse);
    }

    @GetMapping("/cash")
    public ResponseEntity<MemberShowCurrentCashResponse> showCurrentCash(@AuthPrincipal AuthMember authMember) {
        MemberShowCurrentCashResponse currentCashResponse = memberService.findMemberCurrentCharge(authMember);
        return ResponseEntity.ok(currentCashResponse);
    }
}
