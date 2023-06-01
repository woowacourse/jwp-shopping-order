package cart.domain;

public class Member {
    private Long id;
    private String email;
    private String password;
    private Point point;

    public Member(final String email, final String password) {
        this(null, email, password);
    }

    public Member(Long id, String email, String password) {
        this(id, email, password, 5_000);
    }

    public Member(final Long id, final String email, final String password, final int point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = Point.of(point);
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

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public Point getPoint() {
        return point;
    }

    public int getPointAsInt() {
        return point.getValue();
    }
}
