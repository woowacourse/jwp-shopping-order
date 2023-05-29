package cart.application.dto.member;

public class MemberLoginResponse {
    private final String token;

    public MemberLoginResponse(final String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
