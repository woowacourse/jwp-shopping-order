package cart.domain;

public class Member {
    private Long id;
    private String email;
    private String password;
    private Integer point;

    public Member(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Member(final Long id, final String email, final String password, final Integer point) {
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

    public Integer getPoint() {
        return point;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
