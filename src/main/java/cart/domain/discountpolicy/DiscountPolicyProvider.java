package cart.domain.discountpolicy;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class DiscountPolicyProvider {

    private final List<DiscountPolicy> discountPolicies;

    public DiscountPolicyProvider(final List<DiscountPolicy> discountPolicies) {
        this.discountPolicies = discountPolicies;
    }

    public DiscountPolicy getByType(DiscountType type) {
        return discountPolicies.stream()
                .filter(policy -> policy.isSupportTyp(type))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("입력된 할인 정책이 없습니다. - " + type.name()));
    }

    public DiscountType getDiscountType(DiscountPolicy policy) {
        return Arrays.stream(DiscountType.values())
                .filter(policy::isSupportTyp)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("입력된 정책에 빈으로 등록되어있지 않습니다. - " + policy.getClass()));
    }

}
