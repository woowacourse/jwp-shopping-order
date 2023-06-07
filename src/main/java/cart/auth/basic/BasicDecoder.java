package cart.auth.basic;

import org.apache.tomcat.util.codec.binary.Base64;

import java.util.List;

public class BasicDecoder {

    private static final String BASIC_SPLIT = " ";
    private static final String USERNAME_PASSWORD_SPLIT = ":";
    private static final int BASIC_HEADER = 1;
    private static final int USERNAME = 0;
    private static final int PASSWORD = 1;

    private BasicDecoder() {
    }

    public static List<String> decode(String basicAuthorization) {
        String[] basicHeader = basicAuthorization.split(BASIC_SPLIT);
        byte[] decodeHeader = Base64.decodeBase64(basicHeader[BASIC_HEADER]);
        String credentials = new String(decodeHeader);
        String[] usernameAndPassword = credentials.split(USERNAME_PASSWORD_SPLIT);

        return List.of(usernameAndPassword[USERNAME], usernameAndPassword[PASSWORD]);
    }
}
