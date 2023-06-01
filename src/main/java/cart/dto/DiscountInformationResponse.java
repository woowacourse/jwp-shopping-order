package cart.dto;

public final class DiscountInformationResponse {
    private final String policyName;
    private final double discountRate;
    private final int discountPrice;

    public DiscountInformationResponse(final String policyName, final double discountRate, final int discountPrice) {
        this.policyName = policyName;
        this.discountRate = discountRate;
        this.discountPrice = discountPrice;
    }
}
