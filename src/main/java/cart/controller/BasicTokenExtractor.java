package cart.controller;

import cart.exception.AuthenticationException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class BasicTokenExtractor {

    private static final String BASIC_AUTH_PREFIX = "Basic ";


    public String[] decode(String authorization) {
        if (!authorization.startsWith(BASIC_AUTH_PREFIX)) {
            throw new AuthenticationException("잘못된 인증 방식입니다.");
        }
        String token = authorization.substring(BASIC_AUTH_PREFIX.length());
        String decodeToken = decodeToken(token);
        return decodeToken.split(":");
    }

    private String decodeToken(String token) {
        try {
            return new String(Base64.decodeBase64(token));
        } catch (IllegalStateException e) {
            throw new AuthenticationException("토큰을 복호화 할 수 없습니다.");
        }
    }
}
