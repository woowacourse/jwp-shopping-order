package cart.domain.member;

import cart.exception.point.PointAbusedException;

import java.util.Objects;

public class Member {

    private final Long id;
    private final MemberEmail email;
    private final MemberPassword password;
    private final MemberPoint point;

    public Member(final String email, final String password) {
        this(null, email, password);
    }

    public Member(final Long id, final String email, final String password) {
        this(id, email, password, 0);
    }

    public Member(final Long id, final String email, final String password, final Integer point) {
        this.id = id;
        this.email = new MemberEmail(email);
        this.password = new MemberPassword(password);
        this.point = new MemberPoint(point);
    }

    public Member(final Long id, final MemberEmail email, final MemberPassword password, final MemberPoint point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(new MemberPassword(password));
    }

    public Member updatePoint(final MemberPoint requestedPoint, final int totalPrice) {
        if (point.isLowerThan(requestedPoint)) {
            throw new PointAbusedException(point, requestedPoint);
        }
        final MemberPoint minusPoint = this.point.minus(requestedPoint);
        final MemberPoint resultPoint = minusPoint.addPointByTotalPrice(totalPrice);

        return new Member(id, email, password, resultPoint);
    }

    public Long getId() {
        return id;
    }

    public MemberEmail getEmail() {
        return email;
    }

    public String getEmailValue() {
        return email.getEmail();
    }

    public MemberPassword getPassword() {
        return password;
    }

    public String getPasswordValue() {
        return password.getPassword();
    }

    public MemberPoint getPoint() {
        return point;
    }

    public Integer getPointValue() {
        return point.getPoint();
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
