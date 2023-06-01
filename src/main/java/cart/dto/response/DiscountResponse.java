package cart.dto.response;

import cart.application.DiscountService;
import cart.domain.Grade;
import cart.domain.PriceDiscountCalculator;

public class DiscountResponse {

    private final String policyName;
    private final double discountRate;
    private final int discountPrice;

    private DiscountResponse(final String policyName, final double discountRate, final int discountPrice) {
        this.policyName = policyName;
        this.discountRate = discountRate;
        this.discountPrice = discountPrice;
    }

    public static DiscountResponse of(final Grade grade, final int price) {
        return new DiscountResponse(grade.getName(), grade.getDiscountRate(), price);
    }

    public static DiscountResponse from(final PriceDiscountCalculator priceDiscountCalculator) {
        return new DiscountResponse(priceDiscountCalculator.getPolicyName(), priceDiscountCalculator.discountRate(), priceDiscountCalculator.getDiscountedPrice());
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
