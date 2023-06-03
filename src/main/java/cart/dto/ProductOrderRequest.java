package cart.dto;

public class ProductOrderRequest {

    private Long productId;
    private int quantity;

    public ProductOrderRequest() {
    }

    public ProductOrderRequest(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
