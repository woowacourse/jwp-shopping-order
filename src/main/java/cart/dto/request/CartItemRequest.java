package cart.dto.request;

public class CartItemRequest {
    private final Long id;
    private final Long productId;
    private final int quantity;

    public CartItemRequest(final Long id, final Long productId, final int quantity) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
