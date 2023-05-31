package cart.domain;

public class Member {
    private Long id;
    private String email;
    private String password;
    private Point point;

    public Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = new Point(0L);
    }

    public Member(Long id, String email, String password, Point point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
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

    public boolean checkPassword(String password) {
        return this.password.equals(password);
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
