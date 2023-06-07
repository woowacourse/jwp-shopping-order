package cart.domain.value;

import cart.exception.value.NullOrBlankException;
import cart.exception.value.password.InvalidPasswordException;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Password {

    private static final Pattern PASSWORD_REGEX = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*#?&])[a-zA-Z\\d@$!%*#?&]{8,16}$");

    private final String password;

    public Password(final String password) {
        validateNullOrBlank(password);
        validatePassword(password);
        this.password = password;
    }

    private void validateNullOrBlank(final String password) {
        if (ObjectUtils.isEmpty(password) || password.isBlank()) {
            throw new NullOrBlankException("비밀번호");
        }
    }

    private void validatePassword(final String password) {
        Matcher matcher = PASSWORD_REGEX.matcher(password);
        if (!matcher.matches()) {
            throw new InvalidPasswordException();
        }
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password1 = (Password) o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
