package cart.dto.request;

import java.beans.ConstructorProperties;

public class LoginRequest {

    private final String email;
    private final String password;

    @ConstructorProperties(value = {"email", "password"})
    public LoginRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
