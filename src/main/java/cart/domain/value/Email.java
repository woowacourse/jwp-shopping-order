package cart.domain.value;

import org.springframework.util.ObjectUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Email {
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private final String email;

    public Email(final String email) {
        validateNullOrBlank(email);
        validateEmailForm(email);
        this.email = email;
    }

    private void validateNullOrBlank(final String email) {
        if (ObjectUtils.isEmpty(email) || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 null이나 빈 값이 될 수 없습니다.");
        }
    }

    private void validateEmailForm(final String email) {
        Matcher matcher = EMAIL_REGEX.matcher(email);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다.");
        }
    }

    public String getEmail() {
        return email;
    }
}
