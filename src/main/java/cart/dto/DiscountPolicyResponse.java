package cart.dto;

import java.util.Map;

import cart.domain.DiscountPolicy;

public class DiscountPolicyResponse {

    private static Map<DiscountPolicy, String> DISCOUNT_POLICY_NAMES = Map.of(
            DiscountPolicy.FIXED, "price",
            DiscountPolicy.PERCENTAGE, "rate"
    );

    private String type;
    private Integer amount;

    public DiscountPolicyResponse(String type, Integer amount) {
        this.type = type;
        this.amount = amount;
    }

    public static DiscountPolicyResponse of(DiscountPolicy discountPolicy) {
        return new DiscountPolicyResponse(
                DISCOUNT_POLICY_NAMES.get(discountPolicy),
                0
        );
    }

    public DiscountPolicyResponse withAmount(Integer amount) {
        return new DiscountPolicyResponse(
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
