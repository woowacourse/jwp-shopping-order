package cart.ui.member.dto.response;

public class MemberLoginResponse {
    private String token;

    private MemberLoginResponse() {
    }

    public MemberLoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
