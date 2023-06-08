package cart.domain;

public class Member {
    private final Long id;
    private final String email;
    private final String password;

    public Member(final Long id, final String email, final String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Member(final long id) {
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
