package cart.domain.member;

import static cart.exception.ErrorCode.MEMBER_PASSWORD_LENGTH;

import cart.exception.BadRequestException;
import java.util.Objects;

public class NaturalPassword implements MemberPassword {

    private static final int PASSWORD_MIN_LENGTH = 4, PASSWORD_MAX_LENGTH = 10;

    private final String password;

    private NaturalPassword(final String password) {
        this.password = password;
    }

    public static NaturalPassword create(final String password) {
        validatePassword(password);
        return new NaturalPassword(password);
    }

    private static void validatePassword(final String password) {
        if (password.length() < PASSWORD_MIN_LENGTH || password.length() > PASSWORD_MAX_LENGTH) {
            throw new BadRequestException(MEMBER_PASSWORD_LENGTH);
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
        final NaturalPassword that = (NaturalPassword) o;
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
