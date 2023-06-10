package cart.dto.order.discountpolicy;

import java.util.List;

public class DiscountPolicyResponse {

    private List<FixedDiscountPolicyResponse> fixedDiscountPolicy;

    public DiscountPolicyResponse() {
    }

    public DiscountPolicyResponse(final List<FixedDiscountPolicyResponse> fixedDiscountPolicy) {
        this.fixedDiscountPolicy = fixedDiscountPolicy;
    }

    public List<FixedDiscountPolicyResponse> getFixedDiscountPolicy() {
        return fixedDiscountPolicy;
    }
}
