package cart.domain.member;

import cart.domain.payment.Payment;
import cart.domain.payment.PointPolicy;
import cart.domain.vo.Cash;
import cart.domain.vo.Point;
import cart.exception.AuthenticationException;
import cart.exception.MemberException;

import java.util.Objects;
import java.util.regex.Pattern;

public class Member {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$");

    private Long id;
    private final String email;
    private final String password;
    private Point availablePoint;
    private Cash availableMoney;

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
        this.availablePoint = new Point();
        this.availableMoney = new Cash();
    }

    public Member(Long id, String email, String password) {
        validate(id, email);
        this.id = id;
        this.email = email;
        this.password = password;
        this.availablePoint = new Point();
        this.availableMoney = new Cash();
    }

    public Member(Long id, String email, String password, Point point, Cash cash) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.availablePoint = point;
        this.availableMoney = cash;
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
        availablePoint = availablePoint.consume(payment.getUsedPoint());
        availablePoint = availablePoint.earnPoint(PointPolicy.EARNING_POINT_RATIO, payment.getUserPayment());
        availableMoney = availableMoney.consume(payment.getUserPayment());
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

    public void checkPassword(String password) {
        if (!this.password.equals(password)) {
            throw new AuthenticationException("잘못된 사용자입니다.");
        }
    }

    public int getAvailablePoint() {
        return availablePoint.getPoint();
    }

    public int getAvailableMoney() {
        return availableMoney.getCash();
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", point=" + availablePoint +
                '}';
    }
}
