package cart.domain.member;

import java.util.Objects;

public class Member {

    private final Long id;
    private final MemberEmail email;
    private final MemberPassword password;
    private final MemberPoint point;

    public Member(final Long id, final String email, final String password) {
        this(id, email, password, 0);
    }

    public Member(final Long id, final String email, final String password, final Integer point) {
        this.id = id;
        this.email = new MemberEmail(email);
        this.password = new MemberPassword(password);
        this.point = new MemberPoint(point);
    }

    public boolean checkPassword(String password) {
        return this.password.equals(new MemberPassword(password));
    }

    public Long getId() {
        return id;
    }

    public String getEmailValue() {
        return email.getEmail();
    }

    public String getPasswordValue() {
        return password.getPassword();
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
}
