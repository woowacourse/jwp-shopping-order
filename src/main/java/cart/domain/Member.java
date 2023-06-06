package cart.domain;

import java.util.Objects;

public class Member {

    private static final int ZERO_POINTS = 0;

    private final Long id;
    private final String email;
    private final String password;
    private final int points;

    public static Member of(final Long id, final String email, final String password) {
        return new Member(id, email, password, ZERO_POINTS);
    }

    public static Member of(final Long id, final String email, final String password, final int points) {
        return new Member(id, email, password, points);
    }

    private Member(final Long id, final String email, final String password, final int points) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.points = points;
    }

    public Member updatePoints(final int addedPoints, final int usedPoints) {
        final int totalPoints = points + addedPoints - usedPoints;
        return new Member(id, email, password, totalPoints);
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member other = (Member) o;
        if (id == null || other.id == null) {
            return false;
        }
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
