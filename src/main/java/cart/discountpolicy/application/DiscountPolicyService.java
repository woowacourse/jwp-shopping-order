package cart.discountpolicy.application;

import cart.discountpolicy.discountcondition.DiscountCondition;
import org.springframework.stereotype.Service;

@Service
public class DiscountPolicyService {
    private final DiscountPolicyRepository discountPolicyRepository;

    public DiscountPolicyService(DiscountPolicyRepository discountPolicyRepository) {
        this.discountPolicyRepository = discountPolicyRepository;
    }

    public Long savePolicy(DiscountCondition discountCondition) {
        return discountPolicyRepository.save(discountCondition);
    }
}
