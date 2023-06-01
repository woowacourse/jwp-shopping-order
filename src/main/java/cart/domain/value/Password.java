package cart.domain.value;

import java.util.Objects;
import java.util.regex.Pattern;

public class Password {

    private static final Pattern PASSWORD_REGEX = Pattern.compile(
            "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*#?&])[a-zA-Z\\d@$!%*#?&]{8,16}$");

    private final String value;

    public Password(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (!PASSWORD_REGEX.matcher(value).matches()) {
            throw new IllegalArgumentException("비밀번호는 8~16자의 영문 대 소문자, 숫자, 특수문자를 사용해야합니다.");
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
        final Password password = (Password) o;
        return Objects.equals(getValue(), password.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
