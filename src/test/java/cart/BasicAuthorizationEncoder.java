package cart;

import org.apache.tomcat.util.codec.binary.Base64;

public class BasicAuthorizationEncoder {

    private static final String BASIC_TYPE = "Basic";
    private static final String DELIMITER = ":";

    public static String encode(final String email, final String password) {
        final String data = email + DELIMITER + password;
        return BASIC_TYPE + " " + Base64.encodeBase64String(data.getBytes());
    }
}
