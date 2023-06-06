package cart.domain.coupon;

import cart.domain.Money;
import cart.domain.coupon.discountPolicy.DiscountPolicy;

import java.math.BigDecimal;
import java.util.Objects;

public class Coupon {

    private final Long id;
    private final String name;
    private final DiscountPolicy discountPolicy;
    private final BigDecimal discountValue;
    private final Money minimumPrice;

    public Coupon(final Long id, final String name, final DiscountPolicy discountPolicy, final BigDecimal discountValue, final Money minimumPrice) {
        this.id = id;
        this.name = name;
        this.discountPolicy = discountPolicy;
        this.discountValue = discountValue;
        this.minimumPrice = minimumPrice;
    }

    public Coupon(final String name, final DiscountPolicy discountPolicy, final BigDecimal discountValue, final Money minimumPrice) {
        this(null, name, discountPolicy, discountValue, minimumPrice);
    }

    public Money discount(final Money money) {
        if (money.isMoreThan(minimumPrice)) {
            return discountPolicy.discount(money, discountValue);
        }
        return new Money(BigDecimal.ZERO);
    }

    public Money discountDeliveryFee(final Money orderPrice, final Money deliveryFee) {
        if (orderPrice.isMoreThan(minimumPrice)) {
            return deliveryFee;
        }
        return new Money(BigDecimal.ZERO);
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

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public Money getMinimumPrice() {
        return minimumPrice;
    }

}
