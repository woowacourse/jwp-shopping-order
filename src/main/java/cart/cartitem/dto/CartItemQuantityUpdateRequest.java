package cart.cartitem.dto;

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
    
    @Override
    public String toString() {
        return "CartItemQuantityUpdateRequest{" +
                "quantity=" + quantity +
                '}';
    }
}
