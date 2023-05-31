package cart.dto.request;

public class CartItemRequest {
    private Long productId;

    public CartItemRequest() {
    }

    public CartItemRequest(final Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
