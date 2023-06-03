package cart.discountpolicy.application;

import cart.discountpolicy.DiscountPolicy;
import cart.discountpolicy.discountcondition.DiscountCondition;

public interface DiscountPolicyRepository {
    long save(DiscountCondition discountCondition);

    DiscountPolicy findById(Long id);
}
