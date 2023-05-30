package cart.dao;

public class MemberEntity {
    private final Long id;
    private final String email;
    private final String password;
    private final Integer point;

    public MemberEntity(final Long id, final String email, final String password, final Integer point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
    }

    public MemberEntity(final String email, final String password, final Integer point) {
        this(null, email, password, point);
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
}
