package com.woowahan.techcourse.member.ui;

public class AuthCredentials {

    private static final int EMAIL_INDEX = 0;
    private static final int PASSWORD_INDEX = 1;

    private final String email;
    private final String password;

    public AuthCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public AuthCredentials(String[] credentials) {
        this(credentials[EMAIL_INDEX], credentials[PASSWORD_INDEX]);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
