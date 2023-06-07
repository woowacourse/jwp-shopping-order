package cart.dto.request;

public class CartItemQuantityUpdateRequest {

    private final Integer quantity;

    private CartItemQuantityUpdateRequest() {
        this.quantity = null;
    }

    public CartItemQuantityUpdateRequest(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
