package cart.domain.member;

import static cart.exception.ErrorCode.MEMBER_PASSWORD_LENGTH;

import cart.domain.security.SHA256Service;
import cart.exception.BadRequestException;
import java.util.Objects;

public class MemberPassword {

    private static final int PASSWORD_MIN_LENGTH = 4, PASSWORD_MAX_LENGTH = 10;

    private final String password;

    private MemberPassword(final String password) {
        this.password = password;
    }

    static MemberPassword createWithEncodedPassword(final String encodedPassword) {
        return new MemberPassword(encodedPassword);
    }

    static MemberPassword create(final String password) {
        validatePassword(password);
        final String encodePassword = encodePassword(password);
        return new MemberPassword(encodePassword);
    }

    private static void validatePassword(final String password) {
        if (password.length() < PASSWORD_MIN_LENGTH || password.length() > PASSWORD_MAX_LENGTH) {
            throw new BadRequestException(MEMBER_PASSWORD_LENGTH);
        }
    }

    private static String encodePassword(final String password) {
        return SHA256Service.encrypt(password);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MemberPassword that = (MemberPassword) o;
        return Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }

    public String getPassword() {
        return password;
    }
}
