package shop.web.auth;

import org.springframework.util.Base64Utils;
import org.springframework.web.context.request.NativeWebRequest;
import shop.exception.AuthenticationException;

import javax.servlet.http.HttpServletRequest;

public class BasicAuthorizationExtractor {
    private static final BasicAuthorizationExtractor EXTRACTOR = new BasicAuthorizationExtractor();
    private static final String AUTHORIZATION = "Authorization";
    private static final String BASIC_TYPE = "basic";

    private BasicAuthorizationExtractor() {
    }

    public static BasicAuthorizationExtractor getInstance() {
        return EXTRACTOR;
    }

    public String extract(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);

        validateHeader(header);

        return decodeAuthorizationHeader(header);
    }

    public String extract(NativeWebRequest request) {
        String header = request.getHeader(AUTHORIZATION);

        validateHeader(header);

        return decodeAuthorizationHeader(header);
    }

    private void validateHeader(String header) {
        if (header == null) {
            throw new AuthenticationException("인증 정보가 존재하지 않습니다.");
        }

        if (!header.toLowerCase().startsWith(BASIC_TYPE)) {
            throw new AuthenticationException("인증 정보가 존재하지 않습니다.");
        }
    }

    private String decodeAuthorizationHeader(String header) {
        String authHeaderValue = header.substring(BASIC_TYPE.length()).trim();
        byte[] decodedBytes = Base64Utils.decodeFromUrlSafeString(authHeaderValue);

        return new String(decodedBytes);
    }
}

