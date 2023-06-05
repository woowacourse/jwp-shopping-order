package cart.domain;

import cart.exception.MemberException;

public class Member {

    private final Long id;
    private final String email;
    private final String password;
    private final Money point;

    public Member(String email, String password) {
        this(null, email, password);
    }

    public Member(Long id, String email, String password) {
        this(id, email, password, 0);
    }

    public Member(Long id, String email, String password, int point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = new Money(point);
    }

    private Member(Long id, String email, String password, Money point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void checkUsedPoints(Money usedPoints) {
        if (usedPoints.isGreaterThan(point)) {
            throw new MemberException.TooManyUsedPoints();
        }
    }

    public Member usePoints(Money usedPoints) {
        return new Member(this.id, this.email, this.password, this.point.subtract(usedPoints));
    }

    public Member earnPoints(Money earnedPoints) {
        return new Member(this.id, this.email, this.password, this.point.add(earnedPoints));
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

    public Money point() {
        return point;
    }

    public int getPoint() {
        return point.getValue();
    }
}
