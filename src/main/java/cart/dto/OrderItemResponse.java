package cart.dto;

public class OrderItemResponse {
    private Long id;
    private Integer quantity;
    private ProductResponse productResponse;

    public OrderItemResponse(Long id, Integer quantity, ProductResponse productResponse) {
        this.id = id;
        this.quantity = quantity;
        this.productResponse = productResponse;
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductResponse getProductResponse() {
        return productResponse;
    }
}
