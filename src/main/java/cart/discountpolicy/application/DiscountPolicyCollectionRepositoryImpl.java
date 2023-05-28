package cart.discountpolicy.application;

import cart.discountpolicy.DiscountPolicy;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DiscountPolicyCollectionRepositoryImpl implements DiscountPolicyRepository {
    private final List<DiscountPolicy> discountPolicies;

    public DiscountPolicyCollectionRepositoryImpl(List<DiscountPolicy> discountPolicies) {
        this.discountPolicies = discountPolicies;
    }

    @Override
    public List<DiscountPolicy> findAllNonSelectivePolicies() {
        return discountPolicies.stream()
                .filter(discountPolicy -> !discountPolicy.isSelective())
                .collect(Collectors.toList());
    }
}
