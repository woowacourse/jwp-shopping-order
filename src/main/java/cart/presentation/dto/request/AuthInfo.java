package cart.presentation.dto.request;

public class AuthInfo {

    private final String email;
    private final String password;

    public AuthInfo(String email, String password) {
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
