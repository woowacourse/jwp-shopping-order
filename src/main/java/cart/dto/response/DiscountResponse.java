package cart.dto.response;

import cart.domain.discount.Discount;

public class DiscountResponse {

    private final String policyName;
    private final double discountRate;
    private final int discountPrice;

    private DiscountResponse(final String policyName, final double discountRate, final int discountPrice) {
        this.policyName = policyName;
        this.discountRate = discountRate;
        this.discountPrice = discountPrice;
    }


    public static DiscountResponse from(final Discount discount) {
        return new DiscountResponse(discount.getName(), discount.getRate(), discount.getMoney());
    }

    public String getPolicyName() {
        return policyName;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }
}
