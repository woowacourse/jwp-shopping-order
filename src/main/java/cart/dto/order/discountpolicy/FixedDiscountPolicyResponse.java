package cart.dto.order.discountpolicy;

import cart.domain.order.FixedDiscountPolicy;

public class FixedDiscountPolicyResponse {
    private final int minimumPrice;
    private final int discountPrice;

    private FixedDiscountPolicyResponse(final int minimumPrice, final int discountPrice) {
        this.minimumPrice = minimumPrice;
        this.discountPrice = discountPrice;
    }

    public static FixedDiscountPolicyResponse from(FixedDiscountPolicy fixedDiscountPolicy) {
        return new FixedDiscountPolicyResponse(
                fixedDiscountPolicy.getMinimumPrice(),
                fixedDiscountPolicy.getDiscountPrice());
    }

    public int getMinimumPrice() {
        return minimumPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }
}
