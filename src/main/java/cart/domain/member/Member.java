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

    private Member(Long id, Email email, Password password, Point point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
    }

    public Member assignId(Long id) {
        return new Member(id, email, password, point);
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
