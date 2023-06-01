package cart.dto.login;

public class MemberRequest {

    private String name;
    private String password;

    public MemberRequest() {
    }

    public MemberRequest(final String name, final String password) {
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
