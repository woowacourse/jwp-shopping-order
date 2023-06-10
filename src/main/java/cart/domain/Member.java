package cart.domain;

public class Member {
    private final Long id;
    private final String email;
    private final String password;
    private final Point points;

    public Member(final Long id, final String email, final String password) {
        this(id, email, password, 0);
    }

    public Member(final Long id, final String email, final String password, final int points) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.points = new Point(points);
    }

    public Member chargePoint(final int earnedPoints) {
        return Member.of(this, points.add(earnedPoints));
    }

    public static Member of(final Member member, final Point point) {
        return new Member(member.id, member.email, member.password, point.getPoint());
    }

    public Member spendPoint(final int usedPoints) {
        return Member.of(this, points.subtract(usedPoints));
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
        return points.getPoint();
    }

    public boolean checkPassword(final String password) {
        return this.password.equals(password);
    }
}
