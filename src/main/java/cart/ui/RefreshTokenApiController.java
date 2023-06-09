package cart.ui;

import cart.dto.request.RefreshTokenRequest;
import cart.dto.response.RefreshTokenResponse;
import cart.exception.InvalidTokenException;
import cart.infrastructure.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/refresh-token")
public class RefreshTokenApiController {

    private final JwtTokenProvider jwtTokenProvider;

    public RefreshTokenApiController(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ResponseEntity<RefreshTokenResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        if (!jwtTokenProvider.validateRefreshToken(refreshTokenRequest.getRefreshToken())) {
            throw new InvalidTokenException("유효하지 않은 리프레쉬 토큰입니다.");
        }
        final String accessToken = jwtTokenProvider.createAccessToken(refreshTokenRequest.getEmail());

        return ResponseEntity.ok().body(new RefreshTokenResponse(accessToken));
    }
}
