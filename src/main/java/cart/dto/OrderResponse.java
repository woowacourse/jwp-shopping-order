package cart.dto;

import java.util.List;

public class OrderResponse {

    private final long id;
    private final int originalPrice;
    private final int actualPrice;
    private final int deliveryFee;
    private final List<CartItemDto> cartItems;

    public OrderResponse(long id, int originalPrice, int actualPrice, int deliveryFee, List<CartItemDto> cartItems) {
        this.id = id;
        this.originalPrice = originalPrice;
        this.actualPrice = actualPrice;
        this.deliveryFee = deliveryFee;
        this.cartItems = cartItems;
    }

    public long getId() {
        return id;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public int getActualPrice() {
        return actualPrice;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public List<CartItemDto> getCartItems() {
        return cartItems;
    }
}
