package cart.domain.discountpolicy;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DiscountPolicyProvider {

    private final Map<String, DiscountPolicy> discountPolicyMap;

    public DiscountPolicyProvider(Map<String, DiscountPolicy> discountPolicyMap) {
        this.discountPolicyMap = discountPolicyMap;
    }

    public DiscountPolicy getByType(DiscountType type) {
        return discountPolicyMap.get(type.getBeanName());
    }

    public DiscountType getDiscountType(DiscountPolicy policy) {
        return discountPolicyMap.entrySet().stream()
                .filter(entry -> entry.getValue().getClass() == policy.getClass())
                .map(entry -> DiscountType.of(entry.getKey()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("입력된 정책에 빈으로 등록되어있지 않습니다. - " + policy.getClass()));
    }

}
