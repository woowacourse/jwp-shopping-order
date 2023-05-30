package cart.domain;

public class Member {
    private Long id;
    private String email;
    private String password;

    public Member(final Long id, final String email, final String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean checkPassword(final String password) {
        return this.password.equals(password);
    }
}
