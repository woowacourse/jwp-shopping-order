package cart.discountpolicy;

import cart.discountpolicy.discountcondition.DiscountCondition;

public interface DiscountPolicy {
    boolean isSelective();

    boolean support(DiscountCondition condition);

    int discount(int price);
}
