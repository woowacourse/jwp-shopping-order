package cart.dto.order;

import cart.domain.Price;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FixedDiscountPolicyResponse implements DiscountPolicyResponse {
    private final List<DiscountPolicyDetailResponse> fixedDiscountPolicy;

    private FixedDiscountPolicyResponse(final List<DiscountPolicyDetailResponse> fixedDiscountPolicy) {
        this.fixedDiscountPolicy = fixedDiscountPolicy;
    }

    public static FixedDiscountPolicyResponse from(Map<Price, Price> standards) {
        List<DiscountPolicyDetailResponse> fixedDiscountPolicy = standards.entrySet().stream()
                .map(entry -> DiscountPolicyDetailResponse.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return new FixedDiscountPolicyResponse(fixedDiscountPolicy);
    }

    public List<DiscountPolicyDetailResponse> getFixedDiscountPolicy() {
        return fixedDiscountPolicy;
    }
}
