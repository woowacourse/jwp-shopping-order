package cart.order.presentation;

public class OrderProductsRequest {
    private Long id;
    private int quantity;

    public OrderProductsRequest() {
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }
}
