package cart.domain.member;

import cart.exception.EmailInvalidException;

import java.util.Objects;

public class Email {

    private static final String EMAIL_REGEX = "[0-9a-zA-Z]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";

    private final String email;

    public Email(final String email) {
        validate(email);
        this.email = email;
    }

    private void validate(final String email) {
        if (email.isBlank() || !email.matches(EMAIL_REGEX)) {
            throw new EmailInvalidException(email);
        }
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        Email email1 = (Email) o;
        return Objects.equals(email, email1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
