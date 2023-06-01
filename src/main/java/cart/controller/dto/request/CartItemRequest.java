package cart.controller.dto.request;

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
}
