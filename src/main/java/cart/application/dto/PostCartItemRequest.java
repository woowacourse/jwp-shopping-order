package cart.application.dto;

public class PostCartItemRequest {
    private Long productId;

    public PostCartItemRequest() {
    }

    public PostCartItemRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
