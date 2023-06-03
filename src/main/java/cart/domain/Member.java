package cart.domain;

import cart.exception.point.HavingPointIsLessThanUsePointException;

public class Member {
    private Long id;
    private String email;
    private String password;
    private Point point;

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member(String email, String password, Point point) {
        this.email = email;
        this.password = password;
        this.point = point;
    }

    public Member(Long id, String email, String password, Point point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
    }

    public void usePoint(Point usePoint) {
        if (point.isSmallerThan(usePoint)) {
            throw new HavingPointIsLessThanUsePointException(this.point.getValue(), usePoint.getValue());
        }
        this.point = this.point.subtract(usePoint);
    }

    public void earnPoint(Point earningPoint) {
        this.point = this.point.add(earningPoint);
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
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

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", point=" + point +
                '}';
    }
}
