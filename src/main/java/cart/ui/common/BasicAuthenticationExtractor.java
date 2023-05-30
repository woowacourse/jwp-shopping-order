package cart.ui.common;

import cart.exception.AuthenticationException;
import java.util.Base64;
import org.apache.logging.log4j.util.Strings;

public class BasicAuthenticationExtractor {

    private static final String BASIC_REGEX = "^Basic [A-Za-z0-9+/]+=*$";
    private static final String TYPE_DELIMITER = " ";
    private static final int AUTHORIZATION_VALUE_INDEX = 1;
    private static final String VALUE_DELIMITER = ":";
    private static final int BASIC_AUTH_FIELD_COUNT = 2;

    public MemberAuth extract(String authorization) {
        String encodedAuth = generateEncodedAuth(authorization);
        String[] auth = generateAuth(encodedAuth);
        return new MemberAuth(auth[0], auth[1]);
    }

    private String generateEncodedAuth(String authorization) {
        if (Strings.isBlank(authorization)) {
            throw new AuthenticationException("인증 정보가 존재하지 않습니다.");
        }
        if (!authorization.matches(BASIC_REGEX)) {
            throw new AuthenticationException("인증 정보 형식과 일치하지 않습니다.");
        }
        return authorization.split(TYPE_DELIMITER)[AUTHORIZATION_VALUE_INDEX];
    }

    private String[] generateAuth(String encodedAuth) {
        String decodedAuth = new String(Base64.getDecoder().decode(encodedAuth));
        String[] auth = decodedAuth.split(VALUE_DELIMITER);
        if (auth.length != BASIC_AUTH_FIELD_COUNT) {
            throw new AuthenticationException("유효한 인증 정보 개수와 일치하지 않습니다.");
        }
        return auth;
    }
}
