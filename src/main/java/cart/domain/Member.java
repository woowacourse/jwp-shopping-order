package cart.domain;

import java.util.Objects;

public class Member {

    private final Long id;
    private final String email;
    private final String password;
    private Point point;

    public Member(Long id, String email, String password) {
        this(id, email, password, 0);
    }

    public Member(Long id, String email, String password, int point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = new Point(point);
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void addPoint(final Point point) {
        this.point = this.point.add(point);
    }

    public void usePoint(int point) {
        usePoint(new Point(point));
    }

    public void usePoint(Point point) {
        this.point = this.point.use(point);
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
        final Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
