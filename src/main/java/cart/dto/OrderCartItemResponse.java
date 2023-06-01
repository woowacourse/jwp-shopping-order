package cart.dto;

import cart.domain.OrderCartItem;
import cart.domain.Price;

public class OrderCartItemResponse {
    private final Long cartItemId;
    private final int originalPrice;
    private final int discountPrice;

    public OrderCartItemResponse(Long cartItemId, Price originalPrice, Price discountPrice) {
        this.cartItemId = cartItemId;
        this.originalPrice = originalPrice.getValue();
        this.discountPrice = discountPrice.getValue();
    }

    public static OrderCartItemResponse from(OrderCartItem orderCartItem) {
        return new OrderCartItemResponse(
                orderCartItem.getCartItem().getId(),
                orderCartItem.getOriginalPrice(),
                orderCartItem.getDiscountPrice()
        );
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
