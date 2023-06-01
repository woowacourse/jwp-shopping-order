package cart.service.request;

public class CartItemRequest {

    private final Long productId;
    private final Integer quantity;

    private CartItemRequest() {
        this(null, null);
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
