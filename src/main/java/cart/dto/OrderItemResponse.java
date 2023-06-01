package cart.dto;

public class OrderItemResponse {
    private Long id;
    private Long quantity;
    private ProductResponse productResponse;

    public OrderItemResponse(Long id, Long quantity, ProductResponse productResponse) {
        this.id = id;
        this.quantity = quantity;
        this.productResponse = productResponse;
    }

    public Long getId() {
        return id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public ProductResponse getProductResponse() {
        return productResponse;
    }
}
