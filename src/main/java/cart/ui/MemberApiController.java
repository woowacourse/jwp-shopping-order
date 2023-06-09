package cart.ui;

import cart.application.dto.member.FindProfileResponse;
import cart.domain.Member;
import cart.ui.auth.Login;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping("/members")
public class MemberApiController {

    @GetMapping("/profile")
    public ResponseEntity<FindProfileResponse> getProfile(@Login Member member) {
        return ResponseEntity.ok(FindProfileResponse.from(member));
    }
}
