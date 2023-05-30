package cart.dto;

public class TokenResponse {

    private String token;

    public TokenResponse() {
    }

    public TokenResponse(final String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
