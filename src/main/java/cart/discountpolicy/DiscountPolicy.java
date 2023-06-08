package cart.discountpolicy;

import cart.discountpolicy.application.builder.DiscountUnitPolicy;
import cart.discountpolicy.discountcondition.DiscountCondition;
import cart.discountpolicy.discountcondition.DiscountTarget;

public class DiscountPolicy {
    public final DiscountCondition discountCondition;
    public final DiscountUnitPolicy discountUnitPolicy;

    public DiscountPolicy(DiscountCondition discountCondition, DiscountUnitPolicy discountUnitPolicy) {
        this.discountCondition = discountCondition;
        this.discountUnitPolicy = discountUnitPolicy;
    }

    public boolean isTarget(DiscountTarget target) {
        return this.discountCondition.getDiscountTarget() == target;
    }

    public boolean isApplied(Long productId) {
        return this.discountCondition.
                getDiscountTargetProductIds().contains(productId);
    }

    public int calculateDiscountPrice(int price) {
        return this.discountUnitPolicy.calculateDiscountPrice(this.discountCondition.getDiscountValue(), price);
    }
}

