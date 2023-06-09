package cart.dto;

public class OrderItemRequest {

    private Long cartItemId;
    private int quantity;

    public OrderItemRequest() {
    }

    public OrderItemRequest(Long cartItemId, int quantity) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public int getQuantity() {
        return quantity;
    }
}
