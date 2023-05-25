package cart.domain;

import cart.exception.MemberNotValidException;

public class Member {

    private final Long id;
    private final String email;
    private final String password;

    public Member(final String email, final String password) {
        this(null, email, password);
    }

    public Member(final Long id, final String email, final String password) {
        validate(email, password);
        this.id = id;
        this.email = email;
        this.password = password;
    }

    private void validate(final String email, final String password) {
        validateEmail(email);
        validatePassword(password);
    }

    private void validateEmail(final String email) {
        if (email == null || email.isBlank()) {
            throw new MemberNotValidException("이메일은 공백일 수 없습니다.");
        }
    }

    private void validatePassword(final String password) {
        if (password == null || password.isBlank()) {
            throw new MemberNotValidException("비밀번호는 공백일 수 없습니다.");
        }
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
}
