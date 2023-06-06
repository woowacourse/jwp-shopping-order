package com.woowahan.techcourse.member.ui;

import com.woowahan.techcourse.cart.exception.AuthenticationException;
import org.apache.tomcat.util.codec.binary.Base64;

public class BasicAuthExtractor {

    private BasicAuthExtractor() {
    }

    public static AuthCredentials extractDecodedCredentials(String header) {
        String[] credentials = extractCredentials(header);
        String decodedString = decodeCredentials(credentials);
        return new AuthCredentials(decodedString.split(":"));
    }

    private static String[] extractCredentials(String header) {
        validateHeaderNotNull(header);
        return splitBasicAuthHeader(header);
    }

    private static void validateHeaderNotNull(String header) {
        if (header == null) {
            throw new AuthenticationException();
        }
    }

    private static String[] splitBasicAuthHeader(String header) {
        String[] authHeader = header.split(" ");
        if (!authHeader[0].equalsIgnoreCase("basic")) {
            throw new AuthenticationException();
        }
        return authHeader;
    }

    private static String decodeCredentials(String[] credentials) {
        byte[] decodedBytes = Base64.decodeBase64(credentials[1]);
        return new String(decodedBytes);
    }
}
