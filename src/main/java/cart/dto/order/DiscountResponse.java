package cart.dto.order;

import cart.domain.Money;
import cart.domain.order.payment.DiscountPolicy;

public class DiscountResponse {
    private final String discountPolicy;
    private final int discountAmount;

    public DiscountResponse(String discountPolicy, int discountAmount) {
        this.discountPolicy = discountPolicy;
        this.discountAmount = discountAmount;
    }

    public static DiscountResponse of(DiscountPolicy discountPolicy, Money discountAmount) {
        return new DiscountResponse(discountPolicy.getName(), discountAmount.getValue());
    }

    public String getDiscountPolicy() {
        return discountPolicy;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }
}
