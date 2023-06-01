package cart.domain.value;

import java.util.Objects;
import java.util.regex.Pattern;

public class Email {

    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$");

    private final String value;

    public Email(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (!EMAIL_REGEX.matcher(value).matches()) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Email email = (Email) o;
        return Objects.equals(getValue(), email.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
