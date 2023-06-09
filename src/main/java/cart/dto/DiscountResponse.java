package cart.dto;

import cart.domain.DiscountPolicy;
import cart.domain.Money;

public class DiscountResponse {
    private final String discountPolicy;
    private final int discountAmount;

    public DiscountResponse(String discountPolicy, int discountAmount) {
        this.discountPolicy = discountPolicy;
        this.discountAmount = discountAmount;
    }
    
    public static DiscountResponse  of(DiscountPolicy discountPolicy, Money discountAmount) {
        return new DiscountResponse(discountPolicy.getName(), discountAmount.getValue());
    }

    public String getDiscountPolicy() {
        return discountPolicy;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }
}
