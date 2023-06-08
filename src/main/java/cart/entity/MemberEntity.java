package cart.entity;

public class MemberEntity {

    private final Long id;
    private String email;
    private String password;

    public MemberEntity(Long id) {
        this.id = id;
    }

    public MemberEntity(Long id, String email, String password) {
        this.id = id;
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
}
