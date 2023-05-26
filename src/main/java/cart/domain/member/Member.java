package cart.domain.member;

public class Member {

    private Long id;
    private final Email email;
    private final Password password;

    public Member(final Long id, final String email, final String password) {
        this.id = id;
        this.email = new Email(email);
        this.password = new Password(password);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email.getEmail();
    }

    public String getPassword() {
        return password.getPassword();
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
