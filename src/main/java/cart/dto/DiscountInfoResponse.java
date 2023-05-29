package cart.dto;

public class DiscountInfoResponse {
    private final Double discountRate;
    private final Integer discountPrice;

    public DiscountInfoResponse(Double discountRate, Integer discountPrice) {
        this.discountRate = discountRate;
        this.discountPrice = discountPrice;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }
}
