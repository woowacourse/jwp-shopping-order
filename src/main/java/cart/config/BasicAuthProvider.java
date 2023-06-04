package cart.config;

import cart.application.MemberService;
import cart.dto.User;
import cart.exception.AuthenticationException;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

public class BasicAuthProvider implements AuthProvider {
    private static final String BASIC_HEADER = "Basic ";
    private static final String DELIMITER = ":";
    private static final int EMAIL_INDEX = 0;
    private static final int PASSWORD_INDEX = 1;
    private static final int CREDENTIALS_LENGTH = 2;

    private final MemberService memberService;

    public BasicAuthProvider(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public User resolveUser(String authorization) {
        String[] credentials = getCredentials(authorization);
        validateCredentials(credentials);
        String email = credentials[EMAIL_INDEX];
        String password = credentials[PASSWORD_INDEX];
        return memberService.getAuthenticate(email, password);
    }

    private String[] getCredentials(String authorization) {
        if (!StringUtils.hasText(authorization)) {
            throw new AuthenticationException("인증 헤더가 비어있습니다.");
        }
        if (!authorization.startsWith(BASIC_HEADER)) {
            throw new AuthenticationException("베이직 형식의 인증 정보가 아닙니다.");
        }
        String decodeString = getDecodeString(authorization.substring(BASIC_HEADER.length()));
        return decodeString.split(DELIMITER);
    }

    private String getDecodeString(String encodedString) {
        try {
            return new String(Base64Utils.decodeFromString(encodedString));
        } catch (IllegalArgumentException e) {
            throw new AuthenticationException("올바른 형식의 BASE64 문자열이 아닙니다.");
        }
    }

    private void validateCredentials(String[] credentials) {
        if (credentials.length != CREDENTIALS_LENGTH) {
            throw new AuthenticationException("올바른 형식의 인증 정보가 아닙니다.");
        }
    }
}
