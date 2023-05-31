package cart.application.service;

public class MemberRequest {

    private final String name;
    private final String email;
    private final String password;

    public MemberRequest(final String name, final String email, final String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
