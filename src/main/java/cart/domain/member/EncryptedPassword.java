package cart.domain.member;

import java.util.Objects;

public class EncryptedPassword implements MemberPassword {

    private final String password;

    private EncryptedPassword(final String password) {
        this.password = password;
    }

    public static EncryptedPassword create(final String password) {
        return new EncryptedPassword(password);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EncryptedPassword that = (EncryptedPassword) o;
        return Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }

    @Override
    public String getPassword() {
        return password;
    }
}
