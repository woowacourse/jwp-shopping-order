package cart.auth.dto;

public class AuthorizationDto {

    private final String email;
    private final String password;

    public AuthorizationDto(final String email, final String password) {
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
