package cart.application.dto;

public class MemberLoginRequest {

    private final String name;
    private final String password;

    public MemberLoginRequest(final String name, final String password) {
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
