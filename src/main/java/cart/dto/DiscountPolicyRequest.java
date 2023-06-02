package cart.dto;

import java.util.Map;

import cart.domain.DiscountPolicy;

public class DiscountPolicyRequest {

    private static Map<DiscountPolicy, String> DISCOUNT_POLICY_NAMES = Map.of(
            DiscountPolicy.FIXED, "price",
            DiscountPolicy.PERCENTAGE, "rate"
    );

    private String type;
    private Integer amount;

    public DiscountPolicyRequest(String type, Integer amount) {
        this.type = type;
        this.amount = amount;
    }

    public static DiscountPolicyRequest of(DiscountPolicy discountPolicy) {
        return new DiscountPolicyRequest(
                DISCOUNT_POLICY_NAMES.get(discountPolicy),
                0
        );
    }

    public DiscountPolicyRequest withAmount(Integer amount) {
        return new DiscountPolicyRequest(
                type,
                amount
        );
    }

    public String getType() {
        return type;
    }

    public Integer getAmount() {
        return amount;
    }
}
