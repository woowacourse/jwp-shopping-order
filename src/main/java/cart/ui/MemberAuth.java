package cart.ui;

public class MemberAuth {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;


    public MemberAuth(
            final Long id,
            final String name,
            final String email,
            final String password
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
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
