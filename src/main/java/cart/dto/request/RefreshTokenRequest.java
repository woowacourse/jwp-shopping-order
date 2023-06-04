package cart.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshTokenRequest {

    private String email;
    private String refreshToken;

    public RefreshTokenRequest(final String email, final String refreshToken) {
        this.email = email;
        this.refreshToken = refreshToken;
    }

    public String getEmail() {
        return email;
    }

    @JsonProperty("refresh_token")
    public String getRefreshToken() {
        return refreshToken;
    }
}
