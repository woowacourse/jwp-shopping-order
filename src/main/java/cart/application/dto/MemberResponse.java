package cart.application.dto;

public class MemberResponse {

    private final Long id;
    private final String name;
    private final String password;

    public MemberResponse(final String name, final String password) {
        this(null, name, password);
    }

    public MemberResponse(final Long id, final String name, final String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
