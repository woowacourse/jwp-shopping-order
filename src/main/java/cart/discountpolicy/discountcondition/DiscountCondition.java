package cart.discountpolicy.discountcondition;

import cart.product.Product;

import java.util.List;

public class DiscountCondition {
    private final DiscountTarget discountTarget;
    private final List<Long> discountTargetProductIds;
    private final DiscountUnit discountUnit;
    private int discountValue;

    private DiscountCondition(DiscountTarget discountTarget, List<Long> discountTargetProductIds, DiscountUnit discountUnit, int discountValue) {
        this.discountTarget = discountTarget;
        this.discountTargetProductIds = discountTargetProductIds;
        this.discountUnit = discountUnit;
        this.discountValue = discountValue;
    }

    public static DiscountCondition from(DiscountTarget discountTarget, DiscountUnit discountUnit, int discountValue) {
        return new DiscountCondition(discountTarget, null, discountUnit, discountValue);
    }

    public static DiscountCondition makeConditionForSpecificProducts(List<Long> discountTargetProductIds, DiscountUnit discountUnit, int discountValue) {
        return new DiscountCondition(DiscountTarget.SPECIFIC, discountTargetProductIds, discountUnit, discountValue);
    }

    public DiscountTarget getDiscountTarget() {
        return discountTarget;
    }

    public DiscountUnit getDiscountUnit() {
        return discountUnit;
    }

    public int getDiscountValue() {
        return discountValue;
    }

    public List<Long> getDiscountTargetProductIds() {
        return discountTargetProductIds;
    }
}
