package cart.domain.member;

import cart.exception.BadRequestException;

import static cart.exception.ErrorCode.INVALID_MEMBER_NAME_LENGTH;

public class Member {

    private final Long id;
    private final String name;
    private final Password password;

    public Member(final String name, final Password password) {
        validateName(name);
        this.id = null;
        this.name = name;
        this.password = password;
    }

    private void validateName(final String name) {
        if (name.length() < 4 || name.length() > 10) {
            throw new BadRequestException(INVALID_MEMBER_NAME_LENGTH);
        }
    }

    public Member(final Long id, final String name, final String password) {
        this.id = id;
        this.name = name;
        this.password = Password.of(password);
    }

    public boolean checkPassword(final String password) {
        return this.password.checkPassword(password);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password.getPassword();
    }
}
