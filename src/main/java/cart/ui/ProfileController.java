package cart.ui;

import cart.application.auth.Auth;
import cart.domain.Member;
import cart.dto.response.ProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    @Operation(summary = "프로필 조회 (포인트)")
    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> findProfile(@Auth Member member) {
        final int points = member.getPoints();
        return ResponseEntity.ok(new ProfileResponse(points));
    }
}
