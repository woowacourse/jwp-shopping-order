package cart.dto;

public class CartItemQuantityUpdateRequest {
    private Long quantity;

    public CartItemQuantityUpdateRequest() {
    }

    public CartItemQuantityUpdateRequest(Long quantity) {
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }
}
