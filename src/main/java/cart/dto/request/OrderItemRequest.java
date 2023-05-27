package cart.dto.request;

public class OrderItemRequest {

    private Long id;
    private Integer quantity;

    public OrderItemRequest(final Long id, final Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
