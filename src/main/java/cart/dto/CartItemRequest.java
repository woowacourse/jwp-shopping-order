package cart.dto;

public class CartItemRequest {
    private Long productId;
    private int quantity;

    public CartItemRequest() {
    }

    public CartItemRequest(Long productId, int quantity) {
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
