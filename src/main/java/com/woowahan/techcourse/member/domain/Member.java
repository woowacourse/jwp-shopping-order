package com.woowahan.techcourse.member.domain;

public class Member {

    private final Long id;
    private final String email;
    private final String password;

    public Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean checkPassword(String password) {
        if (this.password == null && password == null) {
            return true;
        }
        if (this.password == null || password == null) {
            return false;
        }
        return this.password.equals(password);
    }
}
