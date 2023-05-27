package cart.dto;

public class CartItemQuantityRequest {

    private int quantity;

    public CartItemQuantityRequest() {
    }

    public CartItemQuantityRequest(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
