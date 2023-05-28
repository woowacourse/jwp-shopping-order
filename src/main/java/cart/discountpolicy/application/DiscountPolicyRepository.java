package cart.discountpolicy.application;

import cart.discountpolicy.DiscountPolicy;

import java.util.List;

public interface DiscountPolicyRepository {
    List<DiscountPolicy> findAllNonSelectivePolicies();
}
