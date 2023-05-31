package cart.domain;

public class Member {
    private final Long id;
    private final String email;
    private final String password;
    private final int points;

    public Member(final Long id, final String email, final String password) {
        this(id, email, password, 0);
    }


    public Member(final Long id, final String email, final String password, final int points) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.points = points;
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

    public int getPoints() {
        return points;
    }

    public boolean checkPassword(final String password) {
        return this.password.equals(password);
    }
}
