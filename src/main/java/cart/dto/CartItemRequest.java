package cart.dto;

public class CartItemRequest {
    private Long productId;

    public CartItemRequest() {
    }

    public CartItemRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
    
    @Override
    public String toString() {
        return "CartItemRequest{" +
                "productId=" + productId +
                '}';
    }
}
