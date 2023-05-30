package cart.domain;

public class Member {

    private final Long id;
    private final String name;
    private final String password;

    public Member(final String name, final String password) {
        this.id = null;
        this.name = name;
        this.password = password;
    }

    public Member(final Long id, final String name, final String password) {
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

    public boolean checkPassword(final String password) {
        return this.password.equals(password);
    }
}
