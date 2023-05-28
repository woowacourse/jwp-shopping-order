package cart.common.auth;

import cart.exception.AuthenticationException;
import java.util.Base64;

public class BasicTokenProvider {

    private static final int BASIC_PREFIX_LENGTH = 6;

    public static String extractToken(final String authorization) {
        if (authorization == null || authorization.length() < BASIC_PREFIX_LENGTH) {
            throw new AuthenticationException();
        }
        final String token = authorization.substring(BASIC_PREFIX_LENGTH);
        final byte[] decodedToken = Base64.getDecoder().decode(token.getBytes());
        return new String(decodedToken);
    }
}
