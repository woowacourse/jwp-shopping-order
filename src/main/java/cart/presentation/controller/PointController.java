package cart.presentation.controller;

import cart.application.MemberService;
import cart.application.domain.Member;
import cart.presentation.dto.request.AuthInfo;
import cart.presentation.dto.response.PointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/point")
public class PointController {

    private final MemberService memberService;

    public PointController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<PointResponse> getPoint(AuthInfo authInfo) {
        PointResponse response = memberService.getPoint(authInfo);
        return ResponseEntity.ok(response);
    }
}
