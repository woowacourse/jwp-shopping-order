package cart.domain;

public class Member {

    private final Long id;
    private final String email;
    private final Password password;

    public Member(final Long id, final String email, final String password) {
        this.id = id;
        this.email = email;
        this.password = new Password(password);
    }

    public boolean checkPassword(final Password password) {
        return this.password.equals(password);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public String getPasswordValue() {
        return password.getValue();
    }
}
