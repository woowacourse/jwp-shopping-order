package cart.repository;

import cart.domain.DiscountPolicy;

import java.util.List;

public class DiscountPolicyDbRepository implements DiscountPolicyRepository {
    @Override
    public List<DiscountPolicy> findDefault() {
        return null;
    }
}
