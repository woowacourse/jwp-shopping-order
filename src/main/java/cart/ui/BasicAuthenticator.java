package cart.ui;

import cart.application.MemberService;
import cart.domain.Member;
import cart.exception.AuthenticationException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class BasicAuthenticator implements MemberAuthenticator {
    private static final int SCHEME_INDEX = 0;
    private static final int AUTHORIZATION_INDEX = 1;
    private static final String BASIC_AUTHENTICATION_HEADER = "basic";

    private final MemberService memberService;

    public BasicAuthenticator(final MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public Member findAuthenticatedMember(final String credential) {
        final String[] authHeader = credential.split(" ");
        validateBasicAuthenticationScheme(authHeader);

        final byte[] decodedBytes = Base64.decodeBase64(authHeader[AUTHORIZATION_INDEX]);
        final String decodedString = new String(decodedBytes);

        final String[] credentials = decodedString.split(":");
        final String email = credentials[0];
        final String password = credentials[1];

        // 본인 여부 확인
        return memberService.findAuthorizedMember(email, password);
    }

    private void validateBasicAuthenticationScheme(final String[] authHeader) {
        if (!authHeader[SCHEME_INDEX].equalsIgnoreCase(BASIC_AUTHENTICATION_HEADER)) {
            throw new AuthenticationException("유효하지 않은 인증 스킴입니다.");
        }
    }
}
