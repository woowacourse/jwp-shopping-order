package cart.dto.request;

public class CartItemRequest {
    private Long productId;
    private Integer quantity;

    public CartItemRequest() {
    }

    public CartItemRequest(final Long productId) {
        this.productId = productId;
        this.quantity = 1;
    }

    public CartItemRequest(final Long productId, final Integer quantity) {
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
