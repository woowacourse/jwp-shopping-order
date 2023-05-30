package cart.domain.member;

import cart.domain.Point;

import java.util.Objects;

public class Member {

    private final Long id;
    private final Email email;
    private final Password password;
    private final Point point;

    private Member(final Long id, final Email email, final Password password, final Point point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
    }

    public Member(final Long id, final String email, final String password, final int point) {
        this(id, new Email(email), new Password(password), Point.valueOf(point));
    }

    public boolean hasSamePassword(final Password password) {
        return this.password.equals(password);
    }

    public boolean canUsePoint(final Point point) {
        return this.point.isMoreThan(point);
    }

    public Member calculatePoint(final Point usePoint, final Point savePoint) {
        final Point reducedPoint = this.point.reduce(usePoint);
        final Point newPoint = reducedPoint.save(savePoint);
        return new Member(id, email, password, newPoint);
    }

    public String getEmailValue() {
        return email.getAddress();
    }

    public String getPasswordValue() {
        return password.getValue();
    }

    public int getPointAmount() {
        return point.getMoneyAmount();
    }

    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public Point getPoint() {
        return point;
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
        return Objects.equals(id, member.id) && Objects.equals(email, member.email) && Objects.equals(password, member.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email=" + email +
                ", password=" + password +
                ", point=" + point +
                '}';
    }
}
