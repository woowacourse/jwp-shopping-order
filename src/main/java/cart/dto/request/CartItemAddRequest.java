package cart.dto.request;

public class CartItemAddRequest {

    private final Long productId;

    private CartItemAddRequest() {
        this.productId = null;
    }

    public CartItemAddRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
