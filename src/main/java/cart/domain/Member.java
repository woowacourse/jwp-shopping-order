package cart.domain;

public class Member {
    private Long id;
    private String email;
    private String password;

    private Point point;

    public Member(Long id, String email, String password, Point point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
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
}
