package cart.controller.order.dto;

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
