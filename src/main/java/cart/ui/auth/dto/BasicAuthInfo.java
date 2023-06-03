package cart.ui.auth.dto;

public class BasicAuthInfo {

    private final String email;
    private final String password;

    public BasicAuthInfo(final String email, final String password) {
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
