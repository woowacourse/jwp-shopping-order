package cart.discountpolicy.application.builder;

import cart.cart.Cart;
import cart.discountpolicy.DiscountPolicy;
import cart.discountpolicy.discountcondition.DiscountCondition;

public abstract class DiscountTargetPolicy implements DiscountPolicy {
    protected final DiscountCondition discountCondition;
    protected final DiscountUnitPolicy discountUnitPolicy;

    public DiscountTargetPolicy(DiscountCondition discountCondition, DiscountUnitPolicy discountUnitPolicy) {
        this.discountCondition = discountCondition;
        this.discountUnitPolicy = discountUnitPolicy;
    }

    public abstract void discount(Cart cart);
}
