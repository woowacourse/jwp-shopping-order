package cart.domain.member;

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

    public boolean checkPassword(String password) {
        return this.password.equals(new MemberPassword(password));
    }
}
