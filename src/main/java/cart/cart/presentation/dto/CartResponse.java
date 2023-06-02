package cart.cart.presentation.dto;

import java.util.List;

public class CartResponse {
    private List<CartItemResponse> products;
    private int deliveryPrice;
    private List<CouponResponse> coupons;

    private CartResponse(List<CartItemResponse> cartItemResponses, int deliveryPrice, List<CouponResponse> couponResponses) {
        this.products = cartItemResponses;
        this.deliveryPrice = deliveryPrice;
        this.coupons = couponResponses;
    }

    public List<CartItemResponse> getProducts() {
        return products;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public List<CouponResponse> getCoupons() {
        return coupons;
    }
}
