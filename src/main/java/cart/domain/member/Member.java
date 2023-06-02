package cart.domain.member;

import cart.domain.vo.Money;
import cart.exception.MemberException;

import java.util.Objects;
import java.util.regex.Pattern;

public class Member {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$");

    private final Long id;
    private final String email;
    private final String password;
    private Money money;
    private Money point;

    public Member(String email, String password, Money money, Money point) {
        this(null, email, password, money, point);
    }

    public Member(Long id, String email, String password, Money money, Money point) {
        validate(email);
        this.id = id;
        this.email = email;
        this.password = password;
        this.money = money;
        this.point = point;
    }

    private void validate(String email) {
        if (Objects.isNull(email) || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new MemberException.InvalidEmail();
        }
    }

     public boolean isSamePassword(String password) {
        return this.password.equals(password);
    }

    public boolean isNotSame(Member otherMember) {
        return !this.equals(otherMember);
    }

    public void spendPoint(Money usePoint) {
        checkPointAffordable(usePoint);
        point = point.minus(usePoint);
    }

    private void checkPointAffordable(Money otherPoint) {
        if (!point.isEqualOrGreaterThan(otherPoint)) {
            throw new MemberException.NotEnoughPoint();
        }
    }

    public void spendMoney(Money totalPrice) {
        checkMoneyAffordable(totalPrice);
        money = money.minus(totalPrice);
    }

    private void checkMoneyAffordable(Money otherMoney) {
        if (!money.isEqualOrGreaterThan(otherMoney)) {
            throw new MemberException.NotEnoughMoney();
        }
    }

    public void accumulatePoint(Money newPoint) {
        point = point.plus(newPoint);
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

    public Money getMoney() {
        return money;
    }

    public Money getPoint() {
        return point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(email, member.email) && Objects.equals(password, member.password) && Objects.equals(money, member.money) && Objects.equals(point, member.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, money, point);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", money=" + money +
                ", point=" + point +
                '}';
    }
}
