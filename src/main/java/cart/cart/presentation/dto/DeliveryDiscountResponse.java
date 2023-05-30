package cart.cart.presentation.dto;

import cart.cart.Cart;

public class DeliveryDiscountResponse {
    private long originalPrice;
    private long discountPrice;

    public DeliveryDiscountResponse() {
    }

    public DeliveryDiscountResponse(long originalPrice, long discountPrice) {
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
    }

    public static DeliveryDiscountResponse from(int deliveryPrice) {
        return new DeliveryDiscountResponse(Cart.DEFAULT_DELIVERY_PRICE, Cart.DEFAULT_DELIVERY_PRICE - deliveryPrice);
    }

    public long getOriginalPrice() {
        return originalPrice;
    }

    public long getDiscountPrice() {
        return discountPrice;
    }
}
