package cart.domain.coupon;

import cart.domain.Money;

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

    public Long getId() {
        return id;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public Money getMoney() {
        return money;
    }
}
