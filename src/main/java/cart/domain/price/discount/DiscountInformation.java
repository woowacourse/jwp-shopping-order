package cart.domain.price.discount;

public class DiscountInformation {
    private final String policyName;
    private final Double discountRate;
    private final Integer discountPrice;

    public DiscountInformation(String policyName, Double discountRate, Integer discountPrice) {
        this.policyName = policyName;
        this.discountRate = discountRate;
        this.discountPrice = discountPrice;
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
