package cart.dto;

public class CartItemSaveRequest {

    private Long productId;

    public CartItemSaveRequest() {
    }

    public CartItemSaveRequest(final Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
