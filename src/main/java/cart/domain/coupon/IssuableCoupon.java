package cart.domain.coupon;

import cart.domain.Money;
import java.util.Objects;

public class IssuableCoupon {

    private final Long id;
    private final Coupon coupon;
    private final Money money;

    public IssuableCoupon(Coupon coupon, Money money) {
        this(null, coupon, money);
    }

    public IssuableCoupon(Long id, Coupon coupon, Money money) {
        this.id = id;
        this.coupon = coupon;
        this.money = money;
    }

    public boolean isSatisfied(Money totalOrderPrice) {
        return !totalOrderPrice.isLessThan(money.getValue());
    }

    public boolean isSameCondition(Money money) {
        return this.money.equals(money);
    }

    public Long getId() {
        return id;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public Money getMoney() {
        return money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IssuableCoupon that = (IssuableCoupon) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
