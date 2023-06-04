package cart.dto;

public class ProductOrderRequest {

    private Long productId;
    private Integer quantity;

    public ProductOrderRequest() {
    }

    public ProductOrderRequest(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
