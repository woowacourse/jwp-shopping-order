package cart.domain;

public class Member {
    private static final int MINIMUM_POINT = 0;

    private final Long id;
    private final String email;
    private final String password;
    private Integer point;

    public Member(final Long id, final String email, final String password, final Integer point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
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

    public Integer getPoint() {
        return point;
    }

    public boolean checkPassword(final String password) {
        return this.password.equals(password);
    }

    public boolean isAbleToUsePoint(final Integer usingPoint) {
        return point >= usingPoint;
    }

    private boolean isNegativePoint(final Integer point) {
        return point < MINIMUM_POINT;
    }

    public void usePoint(final Integer usingPoint) {
        if (isNegativePoint(usingPoint)) {
            throw new IllegalArgumentException("사용 가능한 최소 포인트는 " + MINIMUM_POINT + "입니다.");
        }
        if (!isAbleToUsePoint(usingPoint)) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
        point -= usingPoint;
    }

    public void savePoint(final Integer savingPoint) {
        if (isNegativePoint(savingPoint)) {
            throw new IllegalArgumentException("적립 가능한 최소 포인트는" + MINIMUM_POINT + "입니다.");
        }
        point += savingPoint;
    }
}
