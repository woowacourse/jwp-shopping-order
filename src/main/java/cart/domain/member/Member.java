package cart.domain.member;

import java.util.Objects;

public class Member {

    private final Long id;
    private final Email email;
    private final Password password;
    private final Point point;

    public Member(final Long id, final String email, final String password, final int point) {
        this.id = id;
        this.email = new Email(email);
        this.password = new Password(password);
        this.point = new Point(point);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email.getValue();
    }

    public String getPassword() {
        return password.getValue();
    }

    public int getPoint() {
        return point.getValue();
    }

    public boolean checkPassword(final String password) {
        return this.password.checkPassword(password);
    }

    public void usePoint(final Integer usingPoint) {
        point.usePoint(usingPoint);
    }

    public void savePoint(final Integer savingPoint) {
        point.savePoint(savingPoint);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
