package cart.domain.member;

public class Member {

    private final Long id;
    private final Email email;
    private final Password password;

    public Member(final Long id, final Email email, final Password password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public boolean checkPassword(final Password otherPassword) {
        return password.equals(otherPassword);
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
