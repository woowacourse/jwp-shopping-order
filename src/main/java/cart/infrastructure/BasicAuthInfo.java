package cart.infrastructure;

import org.apache.tomcat.util.codec.binary.Base64;

public class BasicAuthInfo implements AuthInfo {

    private static final int CREDENTIALS_EMAIL_INDEX = 0;
    private static final int CREDENTIALS_PASSWORD_INDEX = 1;

    private final String email;
    private final String password;

    private BasicAuthInfo(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public static BasicAuthInfo from(final String authorization) {
        final String[] authHeader = authorization.split(" ");
        if (!authHeader[0].equalsIgnoreCase("basic")) {
            return null;
        }

        final byte[] decodedBytes = Base64.decodeBase64(authHeader[1]);
        final String decodedString = new String(decodedBytes);

        final String[] credentials = decodedString.split(":");
        return new BasicAuthInfo(
                credentials[CREDENTIALS_EMAIL_INDEX],
                credentials[CREDENTIALS_PASSWORD_INDEX]);
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
