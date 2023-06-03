package cart.auth;

import cart.exception.auth.InvalidBasicCredentialException;
import java.util.Base64;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class BasicAuthorizationParser {

    private static final String KEYWORD = "Basic ";
    private static final String DELIMITER = ":";
    private static final int VALID_CREDENTIAL_SIZE = 2;
    private static final int EMAIL_INDEX = 0;
    private static final int PASSWORD_INDEX = 1;
    private static final String EMPTY = "";

    public Credential parse(final String authorizationHeader) {
        if (!StringUtils.hasText(authorizationHeader)) {
            throw new InvalidBasicCredentialException(authorizationHeader);
        }
        final String[] credential = parseCredential(authorizationHeader);
        if (isInvalidBasicCredential(authorizationHeader)) {
            throw new InvalidBasicCredentialException(authorizationHeader);
        }
        return new Credential(credential[EMAIL_INDEX], credential[PASSWORD_INDEX]);
    }

    private String[] parseCredential(final String authorizationHeader) {
        final String credential = authorizationHeader.replace(KEYWORD, EMPTY);
        return decodeBase64(credential).split(DELIMITER);
    }

    private String decodeBase64(final String credential) {
        try {
            return new String(Base64.getDecoder().decode(credential));
        } catch (final IllegalArgumentException e) {
            throw new InvalidBasicCredentialException(credential);
        }
    }

    private boolean isInvalidBasicCredential(final String authorizationHeader) {
        return !authorizationHeader.startsWith(KEYWORD) ||
                parseCredential(authorizationHeader).length != VALID_CREDENTIAL_SIZE;
    }
}

