package cart.dto.product;

public class DeliveryPayResponse {

    private final int originalPrice;
    private final int discountPrice;

    public DeliveryPayResponse(final int originalPrice, final int discountPrice) {
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }
}
