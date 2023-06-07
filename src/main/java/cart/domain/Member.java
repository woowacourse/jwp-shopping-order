package cart.domain;

import java.util.Objects;

public class Member {

    private final Long id;
    private final String email;
    private final String password;
    private final Point point;

    public Member(final Long id, final String email, final String password, final Point point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
    }

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

    public Member usePoint(final Point usePoint) {
        point.validateUsePoint(usePoint);

        Point newPoint = point.subtract(usePoint);

        return new Member(id, email, password, newPoint);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Point getPoint() {
        return point;
    }

    public int getPointValue() {
        return point.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(email, member.email) && Objects.equals(password, member.password) && Objects.equals(point, member.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, point);
    }
}
