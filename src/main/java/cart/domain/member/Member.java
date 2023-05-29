package cart.domain.member;

public class Member {

    private final Long id;
    private final Email email;
    private final Password password;
    private final Point point;

    public Member(String email, String password, int point) {
        this(null, email, password, point);
    }

    public Member(Long id, String email, String password, int point) {
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
}
