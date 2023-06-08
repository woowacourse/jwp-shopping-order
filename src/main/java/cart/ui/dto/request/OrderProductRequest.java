package cart.ui.dto.request;

public class OrderProductRequest {

    private Long id;
    private int quantity;

    public OrderProductRequest() {
    }

    public OrderProductRequest(final Long id, final int quantity) {
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
