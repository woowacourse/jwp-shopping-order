package cart.domain;

import java.util.Objects;

public class Coupon {

    private final Long id;
    private final int amount;
    private final DiscountPolicy discountPolicy;

    public Coupon(Long id, int amount, DiscountPolicy discountPolicy) {
        this.id = id;
        this.amount = amount;
        this.discountPolicy = discountPolicy;
    }

    public Money discount(Money money) {
        return discountPolicy.discount(money, amount);
    }

    public Long getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Coupon coupon = (Coupon)o;

        return Objects.equals(id, coupon.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
