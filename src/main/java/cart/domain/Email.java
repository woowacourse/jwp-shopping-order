package cart.domain;

import java.util.Objects;
import java.util.regex.Pattern;

public class Email {

    private static final Pattern EMAIL = Pattern.compile("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$");

    private final String address;

    public Email(final String address) {
        validateFormat(address);
        this.address = address;
    }

    private void validateFormat(final String address) {
        if (!EMAIL.matcher(address).matches()) {
            throw new IllegalArgumentException("형식에 맞지 않는 이메일입니다.");
        }
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
        return Objects.equals(address, email.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    public String getAddress() {
        return address;
    }
}
