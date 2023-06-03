package cart.controller.cart.dto.dto;

import cart.cartitem.CartItem;

public class CartItemDiscountResponse {
    private long cartItemId;
    private long originalPrice;
    private long discountPrice;

    public CartItemDiscountResponse() {
    }

    public CartItemDiscountResponse(long cartItemId, long originalPrice, long discountPrice) {
        this.cartItemId = cartItemId;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
    }

    public static CartItemDiscountResponse from(CartItem cartItem) {
        return new CartItemDiscountResponse(cartItem.getId(), cartItem.getOriginalPrice(), cartItem.getDiscountPrice());
    }

    public long getCartItemId() {
        return cartItemId;
    }

    public long getOriginalPrice() {
        return originalPrice;
    }

    public long getDiscountPrice() {
        return discountPrice;
    }
}
