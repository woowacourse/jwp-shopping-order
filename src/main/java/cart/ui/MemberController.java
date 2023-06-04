package cart.ui;

import cart.application.MemberService;
import cart.config.Principal;
import cart.dto.User;
import cart.dto.response.PointResponse;
import cart.dto.response.Response;
import cart.dto.response.ResultResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/members")
@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/point")
    public ResponseEntity<Response> inquiryPoint(@Principal User user) {
        long point = memberService.getMemberPoint(user.getMemberId());
        PointResponse pointResponse = new PointResponse(user.getMemberId(), point);
        return ResponseEntity.ok()
                .body(new ResultResponse<>("포인트가 조회되었습니다.", pointResponse));
    }
}
