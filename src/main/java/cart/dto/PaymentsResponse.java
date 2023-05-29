package cart.dto;

import java.util.List;

public class PaymentsResponse {
    private final List<CartItemResponse> cartItems;
    private final int deliveryPrice;
    private final List<CouponResponse> coupons;

    public PaymentsResponse(List<CartItemResponse> cartItems, int deliveryPrice, List<CouponResponse> coupons) {
        this.cartItems = cartItems;
        this.deliveryPrice = deliveryPrice;
        this.coupons = coupons;
    }

    public List<CartItemResponse> getCartItems() {
        return cartItems;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public List<CouponResponse> getCoupons() {
        return coupons;
    }
}
