package cart.ui.member.dto.request;

public class MemberJoinRequest {
    private String name;
    private String password;

    private MemberJoinRequest() {
    }

    public MemberJoinRequest(String name, String password) {
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
