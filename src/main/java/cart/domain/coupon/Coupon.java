package cart.domain.coupon;

import cart.domain.OrderPrice;
import cart.domain.coupon.discountCondition.DiscountCondition;
import cart.domain.coupon.discountPolicy.DiscountPolicy;

public class Coupon {

    private final Long id;
    private final String name;
    private final DiscountPolicy discountPolicy;
    private final DiscountCondition discountCondition;

    public Coupon(final Long id, final String name, final DiscountPolicy discountPolicy, final DiscountCondition discountCondition) {
        this.id = id;
        this.name = name;
        this.discountPolicy = discountPolicy;
        this.discountCondition = discountCondition;
    }

    public OrderPrice apply(final OrderPrice orderPrice) {
        if (discountCondition.isCondition(orderPrice)) {
            return discountPolicy.discount(orderPrice);
        }
        return orderPrice;
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

    public DiscountCondition getDiscountCondition() {
        return discountCondition;
    }
}
