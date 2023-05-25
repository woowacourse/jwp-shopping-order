package cart.domain.discount;

import java.util.List;

import cart.domain.Member;

public class DefaultDiscountPolicy implements DiscountPolicy{

    private final List<DiscountPolicy> policies;

    public DefaultDiscountPolicy(List<DiscountPolicy> policies) {
        this.policies = policies;
    }

    @Override
    public Integer calculateDiscountPrice(Integer price, Member member) {
        return policies.stream()
                .mapToInt(policy -> policy.calculateDiscountPrice(price, member))
                .sum();
    }
}
