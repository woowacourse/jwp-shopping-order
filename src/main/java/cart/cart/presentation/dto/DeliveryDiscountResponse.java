package cart.cart.presentation.dto;

public class DeliveryDiscountResponse {
    private long originalPrice;
    private long discountPrice;

    public DeliveryDiscountResponse() {
    }

    public DeliveryDiscountResponse(long originalPrice, long discountPrice) {
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
    }

    public long getOriginalPrice() {
        return originalPrice;
    }

    public long getDiscountPrice() {
        return discountPrice;
    }
}
