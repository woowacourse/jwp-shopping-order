package cart.ui.member.dto.request;

public class MemberLoginRequest {
    private String name;
    private String password;

    private MemberLoginRequest() {
    }

    public MemberLoginRequest(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
