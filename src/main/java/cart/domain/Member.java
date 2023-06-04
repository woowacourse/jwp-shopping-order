package cart.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Member {

    private final Long id;
    private final String email;
    private final String password;
    private final Point point;

    public Member(final Long id, final String email, final String password) {
        this(id, email, password, new Point(Point.DEFAULT_VALUE));
    }

    public Member(final String email, final String password, final Point point) {
        this(null, email, password, point);
    }

    public Member(final Long id, final String email, final String password, final int point) {
        this(id, email, password, new Point(point));
    }

    public Member(final String email, final String password) {
        this(null, email, password);
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public Member savePoint(final int totalProductPrice) {
        Point newPoint = point.calculatePointBy(totalProductPrice);

        return new Member(id, email, password, point.add(newPoint));
    }

    public int getPointValue() {
        return point.getValue();
    }
}
