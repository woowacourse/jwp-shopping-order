package cart.ui;

import cart.application.dto.member.FindProfileResponse;
import cart.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberApiController {

    @GetMapping("/profile")
    public ResponseEntity<FindProfileResponse> getProfile(Member member) {
        return ResponseEntity.ok(FindProfileResponse.from(member));
    }
}
