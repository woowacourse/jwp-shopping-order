package cart.domain.member;

import java.util.regex.Pattern;

public class Email {

    private static final int MAX_EMAIL_LENGTH = 254;
    private static final String EMAIL_REGX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGX);

    private final String email;

    public Email(final String email) {
        validateEmail(email);
        this.email = email;
    }

    private void validateEmail(final String email) {
        validateBlank(email);
        validateLength(email);
        validatePattern(email);
    }

    private void validateBlank(final String email) {
        if (email.isBlank()) {
            throw new IllegalArgumentException("Email 은 공백일 수 없습니다.");
        }
    }

    private void validateLength(final String email) {
        if (email.length() > MAX_EMAIL_LENGTH) {
            throw new IllegalArgumentException("Email 은 254자를 넘을 수 없습니다.");
        }
    }

    private void validatePattern(final String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Email 형식을 입력해주세요.");
        }
    }

    public String email() {
        return email;
    }
}
