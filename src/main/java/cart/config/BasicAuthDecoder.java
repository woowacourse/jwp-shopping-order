package cart.config;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BasicAuthDecoder {

    private static final String BASIC_PREFIX = "Basic ";

    public static String decode(String authValue) {
        String encodedValue = authValue.replace(BASIC_PREFIX, "");
        return new String(Base64.getDecoder().decode(encodedValue), StandardCharsets.UTF_8);
    }
}
