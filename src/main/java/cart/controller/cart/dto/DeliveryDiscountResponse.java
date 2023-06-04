package cart.controller.cart.dto;

public class DeliveryDiscountResponse {
    private int originalPrice;
    private int discountPrice;

    public DeliveryDiscountResponse() {
    }

    public DeliveryDiscountResponse(int originalPrice, int discountPrice) {
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
