package cart.discountpolicy.application.builder;

import cart.discountpolicy.DiscountPolicy;
import cart.discountpolicy.discountcondition.DiscountCondition;
import cart.discountpolicy.discountcondition.DiscountTarget;
import cart.discountpolicy.discountcondition.DiscountUnit;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class DiscountPolicyBuilder {
    private static final Map<DiscountTarget, Class<? extends DiscountPolicy>> targetPolicies = new HashMap<>();
    private static final Map<DiscountUnit, DiscountUnitPolicy> unitPolicies = new HashMap<>();

    static {
        targetPolicies.put(DiscountTarget.ALL, DiscountForAllProductsPolicy.class);
        targetPolicies.put(DiscountTarget.SPECIFIC, DiscountForSpecificProductsPolicy.class);
        targetPolicies.put(DiscountTarget.DELIVERY, DiscountForDeliveryPolicy.class);
        targetPolicies.put(DiscountTarget.TOTAL, DiscountFromTotalPricePolicy.class);

        unitPolicies.put(DiscountUnit.PERCENTAGE, new PercentageDiscountPolicy());
        unitPolicies.put(DiscountUnit.ABSOLUTE, new AbsoluteDiscountPolicy());
    }

    public static DiscountPolicy build(DiscountCondition discountCondition) {
        try {
            final var discountTargetPolicyConstructor = targetPolicies.get(discountCondition.getDiscountTarget())
                    .getConstructor(DiscountCondition.class, DiscountUnitPolicy.class);
            return discountTargetPolicyConstructor.newInstance(discountCondition, unitPolicies.get(discountCondition.getDiscountUnit()));
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new IllegalArgumentException("주어진 조건에 해당하는 할인정책을 생성할 수 없습니다.");
        }
    }
}
