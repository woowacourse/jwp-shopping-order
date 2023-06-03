package cart.dto;

public class OrderItemRequest {

    private final Long id;
    private final int quantity;

    public OrderItemRequest(Long id, int quantity) {
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
