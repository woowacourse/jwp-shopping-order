package cart.dto;

public class ItemRequest {

    private Long productId;
    private int quantity;

    public ItemRequest() {
    }

    public ItemRequest(final Long productId, final int quantity) {
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
