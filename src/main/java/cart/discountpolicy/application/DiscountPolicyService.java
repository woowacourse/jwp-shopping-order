package cart.discountpolicy.application;

import cart.cart.Cart;
import cart.discountpolicy.DiscountPolicy;
import cart.discountpolicy.application.builder.DiscountPolicyBuilder;
import cart.discountpolicy.discountcondition.DiscountCondition;
import org.springframework.stereotype.Service;

@Service
public class DiscountPolicyService {
    private final DiscountPolicyRepository discountPolicyRepository;

    public DiscountPolicyService(DiscountPolicyRepository discountPolicyRepository) {
        this.discountPolicyRepository = discountPolicyRepository;
    }

    public Long savePolicy(DiscountCondition discountCondition) {
        return discountPolicyRepository.save(DiscountPolicyBuilder.build(discountCondition));
    }

    public void applyPolicy(Long discountConditionId, Cart cart) {
        final var discountPolicy = this.discountPolicyRepository.findById(discountConditionId);
        discountPolicy.discount(cart);
    }

    public int findDiscountPriceFromTotalPrice(Long discountPolicyId, Cart cart) {
        return 5000;
    }
}
