package cart.auth;

import cart.domain.member.Email;
import cart.domain.member.Password;

public class AuthInfo {

    private Email email;
    private Password password;

    public AuthInfo() {
    }

    public AuthInfo(final Email email, final Password password) {
        this.email = email;
        this.password = password;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }
}
