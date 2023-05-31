package cart.domain;

public class Member {
    private Long id;
    private String email;
    private String password;
    private Long point;

    public Member(String email, String password, Long point) {
        this.email = email;
        this.password = password;
        this.point = point;
    }

    public Member(Long id, String email, Long point) {
        this.id = id;
        this.email = email;
        this.point = point;
    }

    public Member(Long id, String email, String password, Long point) {
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

    public Long getPoint() {
        return point;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
