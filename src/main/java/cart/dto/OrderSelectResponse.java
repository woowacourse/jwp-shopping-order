package cart.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderSelectResponse {
    private final Long id;
    private final int originalPrice;
    private final int discountPrice;
    private final int discountedPrice;
    private final List<CartItemResponse> cartItems;
    private final LocalDateTime createdAt;

    public OrderSelectResponse(final Long id,
                               final int originalPrice,
                               final int discountPrice,
                               final int discountedPrice,
                               final List<CartItemResponse> cartItems,
                               final LocalDateTime createdAt) {
        this.id = id;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.discountedPrice = discountedPrice;
        this.cartItems = cartItems;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }

    public List<CartItemResponse> getCartItems() {
        return cartItems;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
