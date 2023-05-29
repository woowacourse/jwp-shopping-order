package cart.domain.member;

public class Member {

    private final Long id;
    private final Email email;
    private final Password password;

    public Member(final Long id, final String email, final String password) {
        this.id = id;
        this.email = new Email(email);
        this.password = new Password(password);
    }

    public boolean checkPassword(final Password password) {
        return this.password.equals(password);
    }

    public String getEmailValue() {
        return email.getAddress();
    }

    public String getPasswordValue() {
        return password.getValue();
    }

    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }
}
