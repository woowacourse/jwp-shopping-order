package cart.auth;

import cart.auth.dto.AuthorizationDto;
import cart.exception.AuthorizationException;
import org.apache.tomcat.util.codec.binary.Base64;

public class BasicAuthorizationExtractor {

    private static final String BASIC_TYPE = "Basic";
    private static final String DELIMITER = ":";
    private static final int CREDENTIALS_LENGTH = 2;

    public static AuthorizationDto extract(final String authorizationHeader) {
        if (String.valueOf(authorizationHeader).startsWith(BASIC_TYPE)) {
            final String[] credentials = decode(authorizationHeader).split(DELIMITER);
            validateCredentials(credentials);
            final String email = credentials[0];
            final String password = credentials[1];
            return new AuthorizationDto(email, password);
        }
        throw new AuthorizationException("Basic Authentication이 아닙니다.");
    }

    private static String decode(final String header) {
        final String authHeaderValue = header.substring(BASIC_TYPE.length()).trim();
        final byte[] decodedBytes = Base64.decodeBase64(authHeaderValue);
        return new String(decodedBytes);
    }

    private static void validateCredentials(final String[] credentials) {
        if (credentials.length != CREDENTIALS_LENGTH) {
            throw new AuthorizationException("잘못된 Authorization 값입니다.");
        }
    }
}
