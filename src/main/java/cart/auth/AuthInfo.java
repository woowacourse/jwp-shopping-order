package cart.auth;

public class AuthInfo {

    private final String email;
    private final String password;

    public AuthInfo(final String email, final String password) {
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
