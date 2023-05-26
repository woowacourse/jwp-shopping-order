package cart.domain;

import cart.exception.MemberException;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Member {

    private static final int MINIMUM_PASSWORD_LENGTH = 1;
    private static final String EMAIL_REGEX = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";

    private final Long id;
    private final String email;
    private final String password;

    public Member(Long id, String email, String password) {
        validateId(id);
        this.id = id;

        validateEmail(email);
        this.email = email;

        validatePassword(password);
        this.password = password;
    }

    private void validateId(Long id) {
        if (Objects.isNull(id)) {
            throw new MemberException.InvalidId("멤버 아이디를 입력해야 합니다.");
        }
    }

    private void validateEmail(String email) {
        if (Objects.isNull(email)) {
            throw new MemberException.InvalidEmail("이메일은 빈 값으로 입력할 수 없습니다.");
        }

        Matcher matcher = Pattern.compile(EMAIL_REGEX).matcher(email);
        if (!matcher.matches()) {
            throw new MemberException.InvalidEmail("이메일 형식을 확인해주세요.");
        }
    }

    private void validatePassword(String password) {
        if (Objects.isNull(password)) {
            throw new MemberException.InvalidPassword("비밀번호는 빈 값으로 입력할 수 없습니다.");
        }
        if (password.length() < MINIMUM_PASSWORD_LENGTH) {
            throw new MemberException.InvalidPassword("비밀번호는 최소 " + MINIMUM_PASSWORD_LENGTH + "자 이상이어야 합니다.");
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

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
