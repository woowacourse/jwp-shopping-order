package cart.ui.auth;

import java.util.Objects;

import cart.exception.AuthenticationException;

public class Credentials {
    private final String email;
    private final String password;

    public Credentials(String email, String password) {
        validateHas(email);
        validateHas(password);
        this.email = email;
        this.password = password;
    }

    private void validateHas(String credential) {
        if (Objects.isNull(credential)) {
            throw new AuthenticationException();
        }
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
