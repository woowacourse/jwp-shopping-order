package cart.dto;

public class CartProductSaveRequest {

    private Long productId;

    public CartProductSaveRequest() {
    }

    public CartProductSaveRequest(final Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
