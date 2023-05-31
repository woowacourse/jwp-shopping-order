package cart.service.request;

public class LoginRequest {

    private final String id;
    private final String password;

    private LoginRequest() {
        this(null, null);
    }

    public LoginRequest(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

}
