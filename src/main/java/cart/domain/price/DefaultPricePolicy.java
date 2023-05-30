package cart.domain.price;

import java.util.List;
import java.util.stream.Collectors;

import cart.domain.Member;
import cart.domain.price.discount.DiscountInformation;
import cart.domain.price.discount.DiscountPolicy;
import cart.domain.price.discount.grade.Grade;

public class DefaultPricePolicy implements PricePolicy {
    private final List<DiscountPolicy> discountPolicies;

    public DefaultPricePolicy(List<DiscountPolicy> discountPolicies) {
        this.discountPolicies = discountPolicies;
    }

    @Override
    public Integer computeAdditionalPrice(Integer price, Member member) {
        return discountPolicies.stream()
                .mapToInt(policy -> policy.calculateDiscountPrice(price, member))
                .sum();
    }

    @Override
    public List<DiscountInformation> getAllDiscountPoliciesInformation(Integer price, Grade grade) {
        return discountPolicies.stream()
                .map(discountPolicy -> discountPolicy.computeDiscountInformation(price, grade))
                .collect(Collectors.toUnmodifiableList());
    }
}
