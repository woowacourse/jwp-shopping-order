package cart.domain;

public class Member {

    private static final int ZERO_POINTS = 0;
    private final Long id;
    private final String email;
    private final String password;
    private final int points;

    public Member(final Long id, final String email, final String password, final int points) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.points = points;
    }

    public Member(final Long id, final String email, final String password) {
        this(id, email, password, ZERO_POINTS);
    }

    public Member updatePoints(final int addedPoints, final int usedPoints) {
        final int totalPoints = points + addedPoints - usedPoints;
        return new Member(id, email, password, totalPoints);
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

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
