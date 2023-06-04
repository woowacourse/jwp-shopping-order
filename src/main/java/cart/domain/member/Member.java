package cart.domain.member;

import cart.domain.payment.Payment;
import cart.domain.payment.Point;
import cart.exception.MemberException;
import cart.exception.NotEnoughMoneyException;
import cart.exception.PointException;

import java.util.Objects;
import java.util.regex.Pattern;

public class Member {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$");
    private static final int INITIAL_POINT = 1_000;
    private static final int MINIMUM_POINT = 0;
    private static final int INITIAL_MONEY = 1_000_000_000;

    private final Long id;
    private final String email;
    private final String password;
    private int point;
    private int money;

    public Member(Long id, String email, String password) {
        validate(id, email);
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = INITIAL_POINT;
        this.money = INITIAL_MONEY;
    }

    public Member(Long id, String email, String password, int money, int point) {
        validate(id, email);
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
        this.money = money;
    }

    private void validate(Long id, String email) {
        validateId(id);
        validateEmail(email);
    }

    private void validateId(Long id) {
        if (Objects.isNull(id)) {
            throw new MemberException.InvalidIdByNull();
        }
    }

    private void validateEmail(String email) {
        if (Objects.isNull(email) || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new MemberException.InvalidEmail();
        }
    }

    public void pay(Payment payment) {
        validatePointToUse(payment.getUsedPoint());
        validateMoneyToUse(payment.getUserPayment());
        point -= payment.getUsedPoint();
        money -= payment.getUserPayment();
        earnPoint(payment);
    }

    private void validatePointToUse(int usePoint) {
        if (usePoint < MINIMUM_POINT || usePoint > point) {
            throw new PointException.InvalidUsedPoint();
        }
    }

    private void validateMoneyToUse(int userPayment) {
        if (userPayment > money) {
            throw new NotEnoughMoneyException();
        }
    }

    private int earnPoint(Payment payment) {
        point += Point.calculateEarningPoint(payment.getUserPayment());
        return point;
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

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public int getPoint() {
        return point;
    }

    public int getMoney() {
        return money;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", point=" + point +
                '}';
    }
}
