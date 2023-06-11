package cart.entity;

public class MemberEntity {
    private final Long id;
    private final String email;
    private final String password;
    private final int point;

    public MemberEntity(Long id, String email, String password, int point) {
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

    public int getPoint() {
        return point;
    }
}
