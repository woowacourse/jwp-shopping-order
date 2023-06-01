package com.woowahan.techcourse.member.ui;

import com.woowahan.techcourse.cart.exception.AuthenticationException;
import org.apache.tomcat.util.codec.binary.Base64;

public class AuthExtractor {

    public static String[] extract(String header) {
        if (header == null) {
            throw new AuthenticationException();
        }
        String[] authHeader = header.split(" ");
        if (!authHeader[0].equalsIgnoreCase("basic")) {
            throw new AuthenticationException();
        }

        byte[] decodedBytes = Base64.decodeBase64(authHeader[1]);
        String decodedString = new String(decodedBytes);

        return decodedString.split(":");
    }
}
