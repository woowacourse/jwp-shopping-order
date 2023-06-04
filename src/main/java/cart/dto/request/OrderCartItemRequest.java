package cart.dto.request;

public class OrderCartItemRequest {

    private Long id;
    private int quantity;

    public OrderCartItemRequest() {
    }

    public OrderCartItemRequest(final Long id, final int quantity) {
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
