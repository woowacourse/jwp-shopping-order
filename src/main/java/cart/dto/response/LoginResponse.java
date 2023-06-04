package cart.dto.response;

import java.beans.ConstructorProperties;

public class LoginResponse {

    private final String accessToken;
    private final String refreshToken;

    @ConstructorProperties(value = {"access_token", "refresh_token"})
    public LoginResponse(final String accessToken, final String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
