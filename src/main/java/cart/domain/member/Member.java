package cart.domain.member;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public class Member {

    private Long id;
    private Email email;
    private Password password;
    private Point point;

    private Member() {
    }

    public Member(String email, String password, int point) {
        this(null, email, password, point);
    }

    public Member(Long id, String email, String password, int point) {
        this.id = id;
        this.email = new Email(email);
        this.password = new Password(password);
        this.point = new Point(point);
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public void usePoint(int point) {
        this.point = this.point.decrease(point);
    }

    public void addPoint(int point) {
        this.point = this.point.increase(point);
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
