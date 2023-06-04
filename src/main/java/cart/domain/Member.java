package cart.domain;

import java.util.Objects;

public class Member {
    private final Long id;
    private final String email;
    private final String password;
    private Point point;

    public Member(Long id, String email, String password, long point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = new Point(point);
    }

    public boolean hasNotEnoughPoint(Point point) {
        return this.point.getAmount() < point.getAmount();
    }

    public void increasePoint(Point point) {
        this.point = this.point.plus(point);
    }

    public void decreasePoint(Point point) {
        this.point = this.point.minus(point);
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public boolean isSameId(Long id) {
        return Objects.equals(this.id, id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(getId(), member.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
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
}
