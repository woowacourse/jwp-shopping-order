package cart.application.dto.request;

public class CartItemRequest {
    private static final int DEFAULT_QUANTITY = 1;
    private Long productId;
    private Integer quantity;

    public CartItemRequest() {
    }

    public CartItemRequest(final Long productId, final Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public CartItemRequest(final Long productId) {
        this(productId, DEFAULT_QUANTITY);
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
