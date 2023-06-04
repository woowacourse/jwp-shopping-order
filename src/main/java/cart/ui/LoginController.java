package cart.ui;

import cart.domain.Member;
import cart.domain.respository.member.MemberRepository;
import cart.dto.request.LoginRequest;
import cart.dto.response.LoginResponse;
import cart.exception.MemberNotExistException;
import cart.infrastructure.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public LoginController(final JwtTokenProvider jwtTokenProvider, final MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@ModelAttribute LoginRequest loginRequest) {
        final Member member = memberRepository.getMemberByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new MemberNotExistException("해당 정보의 멤버가 존재하지 않습니다."));
        member.checkPassword(loginRequest.getPassword());

        final String accessToken = jwtTokenProvider.createAccessToken(member.getEmail());
        final String refreshToken = jwtTokenProvider.createRefreshToken();
        final LoginResponse loginResponse = new LoginResponse(accessToken, refreshToken);

        return ResponseEntity.ok(loginResponse);
    }
}
