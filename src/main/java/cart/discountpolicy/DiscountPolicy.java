package cart.discountpolicy;

import cart.cart.Cart;
import cart.discountpolicy.discountcondition.DiscountCondition;

public abstract class DiscountPolicy {
    private final DiscountCondition discountCondition;

    public DiscountPolicy(DiscountCondition discountCondition) {
        this.discountCondition = discountCondition;
    }

    public abstract void discount(Cart cart);
}
