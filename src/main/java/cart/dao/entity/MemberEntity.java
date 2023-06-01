package cart.dao.entity;

public class MemberEntity {
    private final Long id;
    private final String email;
    private final String password;

    public MemberEntity(final Long id, final String email, final String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public MemberEntity(final long id) {
        this(id, null, null);
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

    public boolean checkPassword(final String password) {
        return this.password.equals(password);
    }
}
