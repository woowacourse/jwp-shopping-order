package cart.dto.request;

public class CartItemRequest {

    private Long productId;
    private int quantity;

    public CartItemRequest() {
    }

    public CartItemRequest(final Long productId, final int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
