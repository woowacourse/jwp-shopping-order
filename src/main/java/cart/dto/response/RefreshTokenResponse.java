package cart.dto.response;

public class RefreshTokenResponse {

    private final String accessToken;

    public RefreshTokenResponse(final String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
