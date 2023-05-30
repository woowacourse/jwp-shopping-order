package cart.dto;

import cart.domain.price.discount.DiscountInformation;

public class DiscountInfoResponse {
    private final String policyName;
    private final Double discountRate;
    private final Integer discountPrice;

    private DiscountInfoResponse(String policyName, Double discountRate, Integer discountPrice) {
        this.policyName = policyName;
        this.discountRate = discountRate;
        this.discountPrice = discountPrice;
    }

    public static DiscountInfoResponse of(DiscountInformation discountInformation) {
        return new DiscountInfoResponse(
                discountInformation.getPolicyName(),
                discountInformation.getDiscountRate(),
                discountInformation.getDiscountPrice()
        );
    }

    public String getPolicyName() {
        return policyName;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }
}
