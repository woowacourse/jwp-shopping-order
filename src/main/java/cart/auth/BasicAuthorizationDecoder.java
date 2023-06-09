package cart.auth;

import java.util.AbstractMap;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;

public class BasicAuthorizationDecoder {
    public static Map.Entry<String, String> decode(String authorization) {
        String[] authHeader = authorization.split(" ");
        if (!authHeader[0].equalsIgnoreCase("basic")) {
            return null;
        }

        byte[] decodedBytes = Base64.decodeBase64(authHeader[1]);
        String decodedString = new String(decodedBytes);

        String[] credentials = decodedString.split(":");
        String email = credentials[0];
        String password = credentials[1];

        return new AbstractMap.SimpleEntry<>(email, password);
    }
}
