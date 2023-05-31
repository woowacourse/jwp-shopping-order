package cart.dto;

public class OrderItemResponse {

    private final Long id;
    private final int quantity;
    private final ProductResponse product;

    private OrderItemResponse(Long id, int quantity, ProductResponse product) {
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
