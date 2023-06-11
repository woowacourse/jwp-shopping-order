package cart.domain;

public class Member {
    private final Long id;
    private final String email;
    private final String password;
    private Point point;

    public Member(Long id, String email, String password, int point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = new Point(point);
    }

    public void usePoint(Point usedPoint) {
        point = point.use(usedPoint);
    }

    public void savePoint(Point savedPoint) {
        point = point.save(savedPoint);
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
