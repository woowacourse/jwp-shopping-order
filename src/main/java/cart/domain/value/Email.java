package cart.domain.value;

import cart.exception.value.NullOrBlankException;
import cart.exception.value.email.InvalidEmailException;
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
            throw new NullOrBlankException("이메일");
        }
    }

    private void validateEmailForm(final String email) {
        Matcher matcher = EMAIL_REGEX.matcher(email);
        if (!matcher.matches()) {
            throw new InvalidEmailException();
        }
    }

    public String getEmail() {
        return email;
    }
}
