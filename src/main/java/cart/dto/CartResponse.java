package cart.dto;

import java.util.List;

public class CartResponse {
    private List<CartItemDto> cartItems;
    private int userPoint;
    private int minUsagePoints;

    public CartResponse(List<CartItemDto> cartItems, int userPoint, int minUsagePoints) {
        this.cartItems = cartItems;
        this.userPoint = userPoint;
        this.minUsagePoints = minUsagePoints;
    }

    public List<CartItemDto> getCartItems() {
        return cartItems;
    }

    public int getUserPoint() {
        return userPoint;
    }

    public int getMinUsagePoints() {
        return minUsagePoints;
    }
}
