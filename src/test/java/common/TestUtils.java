package common;

import org.apache.commons.codec.binary.Base64;

public class TestUtils {

    public static String toBasicAuthHeaderValue(String from) {
        return "Basic " + Base64.encodeBase64String(from.getBytes());
    }
}
