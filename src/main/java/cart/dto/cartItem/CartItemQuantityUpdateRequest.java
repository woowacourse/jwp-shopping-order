package cart.dto.cartItem;

public class CartItemQuantityUpdateRequest {

    private int quantity;

    private CartItemQuantityUpdateRequest() {
    }

    public CartItemQuantityUpdateRequest(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
