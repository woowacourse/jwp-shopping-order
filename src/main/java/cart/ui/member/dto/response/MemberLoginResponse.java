package cart.ui.member.dto.response;

public class MemberLoginResponse {
    private String password;

    private MemberLoginResponse() {
    }

    public MemberLoginResponse(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
