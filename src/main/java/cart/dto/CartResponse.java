package cart.dto;

import java.util.List;

public class CartResponse {
    private List<CartItemResponse> cartItems;
    private int userPoint;
    private int minUsagePoints;

    public CartResponse(List<CartItemResponse> cartItems, int userPoint, int minUsagePoints) {
        this.cartItems = cartItems;
        this.userPoint = userPoint;
        this.minUsagePoints = minUsagePoints;
    }

    public List<CartItemResponse> getCartItems() {
        return cartItems;
    }

    public int getUserPoint() {
        return userPoint;
    }

    public int getMinUsagePoints() {
        return minUsagePoints;
    }
}
