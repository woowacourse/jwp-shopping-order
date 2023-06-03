package cart.domain.coupon;

import cart.domain.Money;
import cart.domain.discountpolicy.DiscountPolicy;
import cart.exception.CouponException;
import cart.exception.IllegalMoneyAmountException;

import java.util.Objects;

public class Coupon {

    private final Long id;
    private final String name;
    private final DiscountPolicy discountPolicy;
    private final double discountValue;

    public Coupon(String name, DiscountPolicy discountPolicy, double discountValue) {
        this.id = null;
        this.name = name;
        this.discountPolicy = discountPolicy;
        this.discountValue = discountValue;
    }

    public Coupon(Long id, String name, DiscountPolicy discountPolicy, double discountValue) {
        this.id = id;
        this.name = name;
        this.discountPolicy = discountPolicy;
        this.discountValue = discountValue;
    }

    public Money apply(Money money) {
        try {
            return discountPolicy.apply(money,  discountValue);
        } catch (IllegalMoneyAmountException exception) {
            return new Money(0);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Coupon coupon = (Coupon) o;
        return Objects.equals(id, coupon.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
