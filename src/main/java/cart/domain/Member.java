package cart.domain;

public class Member {
    private final Long id;
    private final String email;
    private final String password;
    private final Integer point;

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

    public boolean checkPassword(final String password) {
        return this.password.equals(password);
    }

    public boolean isAbleToUsePoint(final Integer usingPoint) {
        return point >= usingPoint;
    }
}
