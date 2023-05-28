package cart.domain.member;

import static cart.exception.ErrorCode.MEMBER_NAME_LENGTH;
import static cart.exception.ErrorCode.MEMBER_PASSWORD_LENGTH;

import cart.exception.BadRequestException;

public class Member {

    private static final int NAME_MIN_LENGTH = 4, NAME_MAX_LENGTH = 10;
    private static final int PASSWORD_MIN_LENGTH = 4, PASSWORD_MAX_LENGTH = 10;

    private final String name;
    private final String password;

    private Member(final String name) {
        this(name, null);
    }

    private Member(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public static Member create(final String name) {
        return new Member(name);
    }

    public static Member create(final String name, final String password) {
        validateName(name);
        validatePassword(password);
        return new Member(name, password);
    }

    private static void validateName(final String name) {
        if (name.length() < NAME_MIN_LENGTH || name.length() > NAME_MAX_LENGTH) {
            throw new BadRequestException(MEMBER_NAME_LENGTH);
        }
    }

    private static void validatePassword(final String nickname) {
        if (nickname.length() < PASSWORD_MIN_LENGTH || nickname.length() > PASSWORD_MAX_LENGTH) {
            throw new BadRequestException(MEMBER_PASSWORD_LENGTH);
        }
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
