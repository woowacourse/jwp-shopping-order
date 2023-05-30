package cart.dto;

import java.util.List;

public class PaymentResponse {
    private final int originalPrice;
    private final List<DiscountResponse> discounts;
    private final int discountedPrice;
    private final int deliveryFee;
    private final int finalPrice;

    public PaymentResponse(int originalPrice, List<DiscountResponse> discounts, int discountedPrice,
                           int deliveryFee, int finalPrice) {
        this.originalPrice = originalPrice;
        this.discounts = discounts;
        this.discountedPrice = discountedPrice;
        this.deliveryFee = deliveryFee;
        this.finalPrice = finalPrice;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public List<DiscountResponse> getDiscounts() {
        return discounts;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public int getFinalPrice() {
        return finalPrice;
    }
}

