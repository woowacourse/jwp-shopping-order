package cart.domain.Member;

import java.util.regex.Pattern;

public class Email {

    private static final int MAX_EMAIL_LENGTH = 150;
    private static final String EMAIL_REGX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGX);

    private final String email;

    public Email(final String email) {

        this.email = email;
    }

    public static Email from(String email) {
        validateEmail(email);
        return new Email(email);
    }

    private static void validateEmail(final String email) {
        validateBlank(email);
        validateLength(email);
        validatePattern(email);
    }

    private static void validateBlank(final String email) {
        if (email.isBlank()) {
            throw new IllegalArgumentException("Email 은 공백일 수 없습니다.");
        }
    }

    private static void validateLength(final String email) {
        if (email.length() > MAX_EMAIL_LENGTH) {
            throw new IllegalArgumentException("Email 은 150자를 넘을 수 없습니다.");
        }
    }

    private static void validatePattern(final String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Email 형식을 입력해주세요.");
        }
    }

    public String email() {
        return email;
    }
}
