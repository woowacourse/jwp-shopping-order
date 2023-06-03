package cart.ui;

import cart.application.MemberService;
import cart.application.dto.member.MemberCouponResponse;
import cart.common.auth.MemberName;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class MemberCouponController {

    private final MemberService memberService;

    public MemberCouponController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/me/coupons")
    public ResponseEntity<List<MemberCouponResponse>> getMyCoupons(@MemberName final String name) {
        return ResponseEntity.ok(memberService.getByCoupons(name));
    }
}
