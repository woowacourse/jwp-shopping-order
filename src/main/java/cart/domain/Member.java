package cart.domain;

import cart.domain.value.Email;
import cart.domain.value.Password;

public class Member {
    private Long id;
    private Email email;
    private Password password;

    public Member(final Long id, final Email email, final Password password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Member(final Long id, final String email, final String password) {
        this(id, new Email(email), new Password(password));
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

    public boolean checkPassword(final Password password) {
        return this.password.equals(password);
    }
}
