package cart.cart.presentation.dto;

import cart.cart.domain.cartitem.CartItem;

public class CartItemDiscountResponse {
    private long productId;
    private long originalPrice;
    private long discountPrice;

    public CartItemDiscountResponse() {
    }

    public CartItemDiscountResponse(long productId, long originalPrice, long discountPrice) {
        this.productId = productId;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
    }

    public static CartItemDiscountResponse from(CartItem cartItem) {
        return new CartItemDiscountResponse(cartItem.getId(), cartItem.getProduct().getPrice(), cartItem.getDiscountPrice());
    }

    public long getProductId() {
        return productId;
    }

    public long getOriginalPrice() {
        return originalPrice;
    }

    public long getDiscountPrice() {
        return discountPrice;
    }
}
