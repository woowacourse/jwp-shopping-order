package cart.domain.member;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Password {

    private static final int MINIMUM_LENGTH = 8;
    private static final int MAXIMUM_LENGTH = 20;
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
    private static final Pattern LETTER_PATTERN = Pattern.compile("[a-zA-Z]");
    private static final Pattern SPECIAL_LETTER_PATTERN = Pattern.compile("[~`!@#$%^&*()-+=]");

    private final String value;

    public Password(final String value) {
//        validateFormat(value);
        this.value = value;
    }

    private void validateFormat(final String value) {
        if (value.length() < MINIMUM_LENGTH || value.length() > MAXIMUM_LENGTH) {
            throw new IllegalArgumentException("비밀번호는 최소 " + MINIMUM_LENGTH + "자 이상, 최대 " + MAXIMUM_LENGTH + "자 이하여야 합니다.");
        }
        if (notContains(NUMBER_PATTERN, value)) {
            throw new IllegalArgumentException("비밀번호는 숫자를 포함해야 합니다.");
        }
        if (notContains(LETTER_PATTERN, value)) {
            throw new IllegalArgumentException("비밀번호는 대소문자를 포함해야 합니다.");
        }
        if (notContains(SPECIAL_LETTER_PATTERN, value)) {
            throw new IllegalArgumentException("비밀번호는 특수문자를 포함해야 합니다.");
        }
    }

    private boolean notContains(final Pattern pattern, final String value) {
        final Matcher matcher = pattern.matcher(value);
        return !matcher.find();
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
        return Objects.equals(value, password.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Password{" +
                "value='" + value + '\'' +
                '}';
    }
}
