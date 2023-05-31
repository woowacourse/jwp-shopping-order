package cart.domain;

public class Member {
    private Long id;
    private String email;
    private String password;
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
