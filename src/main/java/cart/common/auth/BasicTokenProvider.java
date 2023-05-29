package cart.common.auth;

import cart.exception.AuthenticationException;
import java.util.Base64;

public class BasicTokenProvider {

    private static final String DELIMITER = ":";
    private static final int BASIC_PREFIX_LENGTH = 6;

    public static String extractToken(final String authorization) {
        if (authorization == null || authorization.length() < BASIC_PREFIX_LENGTH) {
            throw new AuthenticationException();
        }
        final String token = authorization.substring(BASIC_PREFIX_LENGTH);
        final byte[] decodedToken = Base64.getDecoder().decode(token.getBytes());
        return new String(decodedToken);
    }

    public static String createToken(final String name, final String password) {
        final String rawToken = name + DELIMITER + password;
        return Base64.getEncoder().encodeToString(rawToken.getBytes());
    }
}
