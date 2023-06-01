package cart.dto;

import cart.domain.Price;

public class CartItemPriceResponse {
    private final Long cartItemId;
    private final int originalPrice;
    private final int discountPrice;

    public CartItemPriceResponse(Long cartItemId, Price originalPrice, Price discountPrice) {
        this.cartItemId = cartItemId;
        this.originalPrice = originalPrice.getValue();
        this.discountPrice = discountPrice.getValue();
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }
}
