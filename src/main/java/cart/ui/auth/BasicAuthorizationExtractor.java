package cart.ui.auth;

import java.util.Objects;

import org.apache.tomcat.util.codec.binary.Base64;

import cart.exception.AuthenticationException;

public class BasicAuthorizationExtractor {
    private static final String BASIC_TYPE = "Basic";
    private static final String DELIMITER = ":";
    private static final int NO_LIMIT = -1;

    public static Credentials extract(String authorizationHeader) {
        if (Objects.isNull(authorizationHeader)) {
            throw new AuthenticationException();
        }

        if (authorizationHeader.toLowerCase().startsWith(BASIC_TYPE.toLowerCase())) {
            String authHeaderValue = authorizationHeader.substring(BASIC_TYPE.length()).trim();
            byte[] decodedBytes = Base64.decodeBase64(authHeaderValue);
            String decodedString = new String(decodedBytes);

            String[] credentials = decodedString.split(DELIMITER, NO_LIMIT);
            String email = credentials[0];
            String password = credentials[1];

            return new Credentials(email, password);
        }

        throw new AuthenticationException();
    }

}
