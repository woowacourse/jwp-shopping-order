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

    private Member(final Member member, final Point point) {
        this.id = member.id;
        this.email = member.email;
        this.password = member.password;
        this.points = point;
    }
    
    public Member chargePoint(final int earnedPoints) {
        return new Member(this, points.add(earnedPoints));
    }

    public Member spendPoint(final int usedPoints) {
        return new Member(this, points.subtract(usedPoints));
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
