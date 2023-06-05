package cart.domain;

import cart.exception.MemberException;

import java.util.Objects;
import java.util.regex.Pattern;

public class Member {

    public static final int MINIMUM_PASSWORD_LENGTH = 1;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$");

    private final Long id;
    private final String email;
    private final String password;
    private Money money;
    private Point point;

    public Member(Long id, String email, String password, long money, long point) {
        validate(email, password);
        this.id = id;
        this.email = email;
        this.password = password;
        this.money = new Money(money);
        this.point = new Point(point);
    }

    public Member(Long id, String email, String password) {
        this(id, email, password, 0, 0);
    }

    private void validate(String email, String password) {
        validateEmail(email);
        validatePassword(password);
    }

    private void validatePassword(String password) {
        if (Objects.isNull(password) || password.length() < MINIMUM_PASSWORD_LENGTH) {
            throw new MemberException.InvalidPassword(password);
        }
    }

    private void validateEmail(String email) {
        if (Objects.isNull(email) || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new MemberException.InvalidEmail(email);
        }
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

    public long getMoney() {
        return money.getValue();
    }

    public long getPoint() {
        return point.getValue();
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void addPoint(int point) {
        this.point = this.point.addPoint(point);
    }

    public void usePoint(int usePoint) {
        this.point = this.point.minusPoint(usePoint);
    }

    public void useMoney(long useMoney) {
        this.money = this.money.minusMoney(useMoney);
    }
}
