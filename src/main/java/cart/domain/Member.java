package cart.domain;

import java.util.Objects;

public class Member {
    private Long id;
    private String email;
    private String password;
    private Point point;

    public Member(final String email, final String password) {
        this(null, email, password, 5_000);
    }

    public Member(final String email, final String password, final int point) {
        this(null, email, password, point);
    }

    public Member(final Long id, final String email, final String password, final int point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = Point.of(point);
    }

    public void updatePoint(Point point) {
        this.point = point;
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

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public Point getPoint() {
        return point;
    }

    public int getPointAsInt() {
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
        return Objects.equals(id, member.id) && Objects.equals(email, member.email)
                && Objects.equals(point, member.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, point);
    }
}
