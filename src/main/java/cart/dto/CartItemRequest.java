package cart.dto;

public class CartItemRequest {
    private Long id;
    private int quantity;

    public CartItemRequest() {
    }

    public CartItemRequest(Long id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }
}
