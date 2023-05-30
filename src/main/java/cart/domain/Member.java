package cart.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Member {

    private static final int DEFAULT_POINT_VALUE = 0;

    private final Long id;
    private final String email;
    private final String password;
    private final Integer point;

    public Member(final String email, final String password, final Integer point) {
        this(null, email, password, point);
    }

    public Member(final String email, final String password) {
        this(null, email, password, DEFAULT_POINT_VALUE);
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
