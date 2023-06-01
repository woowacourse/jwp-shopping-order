package cart.controller.dto;

public class OrderItemResponse {
    private final Long id;
    private final int quantity;
    private final ProductResponse product;

    public OrderItemResponse(final Long id, final int quantity, final ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
