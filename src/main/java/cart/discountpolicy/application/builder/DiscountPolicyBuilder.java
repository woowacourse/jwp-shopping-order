package cart.discountpolicy.application.builder;

import cart.discountpolicy.DiscountPolicy;
import cart.discountpolicy.discountcondition.DiscountCondition;
import cart.discountpolicy.discountcondition.DiscountTarget;
import cart.discountpolicy.discountcondition.DiscountUnit;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class DiscountPolicyBuilder {
    private static final Map<DiscountUnit, DiscountUnitPolicy> unitPolicies = new HashMap<>();

    static {
        unitPolicies.put(DiscountUnit.PERCENTAGE, new PercentageDiscountPolicy());
        unitPolicies.put(DiscountUnit.ABSOLUTE, new AbsoluteDiscountPolicy());
    }

    public static DiscountPolicy build(DiscountCondition discountCondition) {
        return new DiscountPolicy(discountCondition, unitPolicies.get(discountCondition.getDiscountUnit()));
    }
}
