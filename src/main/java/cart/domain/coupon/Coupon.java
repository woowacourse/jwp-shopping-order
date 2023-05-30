package cart.domain.coupon;

import cart.domain.TotalPrice;
import cart.domain.coupon.discountPolicy.DiscountPolicy;

public class Coupon {

    private final Long id;
    private final String name;
    private final DiscountPolicy discountPolicy;
    private final long value;
    private final long minimumPrice;

    public Coupon(final Long id, final String name, final DiscountPolicy discountPolicy, final long value, final long minimumPrice) {
        this.id = id;
        this.name = name;
        this.discountPolicy = discountPolicy;
        this.value = value;
        this.minimumPrice = minimumPrice;
    }

    public TotalPrice apply(final TotalPrice totalPrice) {
        if (totalPrice.orderPriceIsMoreThan(minimumPrice)) {
            return discountPolicy.discount(totalPrice, value);
        }
        return totalPrice;
    }

    public TotalPrice calculateDiscountPrice(final TotalPrice totalPrice) {
        return totalPrice.subOrderPrice(discountPolicy.discount(totalPrice, value));
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

    public long getMinimumPrice() {
        return minimumPrice;
    }
}
