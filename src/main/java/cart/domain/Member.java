package cart.domain;

public class Member {
    private Long id;
    private String email;
    private String password;
    private Double point;

    public Member(Long id, String email, String password) {
        this(id, email, password, 0.0);
    }

    public Member(Long id, String email, String password, Double point) {
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

    public Double getPoint() {
        return point;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
