package cart.ui;

import cart.application.MemberService;
import cart.config.auth.Principal;
import cart.dto.User;
import cart.dto.response.PointResponse;
import cart.dto.response.Response;
import cart.dto.response.ResultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Member Controller")
@RequestMapping("/members")
@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ApiOperation(value = "회원의 포인트 조회", authorizations = {@Authorization("Basic")})
    @GetMapping("/point")
    public ResponseEntity<Response> inquiryPoint(@Principal User user) {
        long point = memberService.getMemberPoint(user.getMemberId());
        PointResponse pointResponse = new PointResponse(user.getMemberId(), point);
        return ResponseEntity.ok()
                .body(new ResultResponse<>("포인트가 조회되었습니다.", pointResponse));
    }
}
