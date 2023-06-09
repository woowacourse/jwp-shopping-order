package cart.util;

import org.apache.tomcat.util.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

public class BasicTokenGenerator {

    public static String generate(final String name, final String password) {
        String credentials = name + ":" + password;
        byte[] encodedBytes = Base64.encodeBase64(credentials.getBytes(StandardCharsets.UTF_8));
        return new String(encodedBytes, StandardCharsets.UTF_8);
    }
}
