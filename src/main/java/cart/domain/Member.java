package cart.domain;

import java.util.Objects;

public class Member {
    private final Long id;
    private final String email;
    private final String password;
    private Point point;

    public Member(String email, String password, final int point) {
        this(null, email, password, point);
    }

    public Member(Long id, String email, String password, final int point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = new Point(point);
    }

    public void usePoint(final int point) {
        Point usedPoint = new Point(point);
        this.point = this.point.use(usedPoint);
    }

    public boolean isSamePassword(String password) {
        return this.password.equals(password);
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

    public int getPoint() {
        return point.getValue();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
