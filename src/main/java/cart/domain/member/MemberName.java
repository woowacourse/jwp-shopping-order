package cart.domain.member;

import static cart.exception.ErrorCode.MEMBER_NAME_LENGTH;

import cart.exception.BadRequestException;
import java.util.Objects;

public class MemberName {
    private static final int NAME_MIN_LENGTH = 4, NAME_MAX_LENGTH = 10;

    private final String name;

    private MemberName(final String name) {
        this.name = name;
    }

    public static MemberName create(final String name) {
        validateName(name);
        return new MemberName(name);
    }

    private static void validateName(final String name) {
        if (name.length() < NAME_MIN_LENGTH || name.length() > NAME_MAX_LENGTH) {
            throw new BadRequestException(MEMBER_NAME_LENGTH);
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
        final MemberName that = (MemberName) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }
}
