package cart.domain.coupon;

import cart.domain.Money;
import cart.domain.coupon.discountPolicy.DiscountPolicy;

import java.util.Objects;

public class Coupon {

    private final Long id;
    private final String name;
    private final DiscountPolicy discountPolicy;
    private final long value;
    private final Money minimumPrice;

    public Coupon(final Long id, final String name, final DiscountPolicy discountPolicy, final long value, final Money minimumPrice) {
        this.id = id;
        this.name = name;
        this.discountPolicy = discountPolicy;
        this.value = value;
        this.minimumPrice = minimumPrice;
    }

    public Coupon(final String name, final DiscountPolicy discountPolicy, final long value, final Money minimumPrice) {
        this(null, name, discountPolicy, value, minimumPrice);
    }

    public Money discount(final Money money) {
        if (money.isMoreThan(minimumPrice)) {
            return discountPolicy.discount(money, value);
        }
        return new Money(0);
    }

    public Money discountDeliveryFee(final Money orderPrice, final Money deliveryFee) {
        if (orderPrice.isMoreThan(minimumPrice)) {
            return deliveryFee;
        }
        return new Money(0);
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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    public long getValue() {
        return value;
    }

    public Money getMinimumPrice() {
        return minimumPrice;
    }

}
