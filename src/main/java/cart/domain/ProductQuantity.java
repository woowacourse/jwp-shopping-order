package cart.domain;

public class ProductQuantity {
    private final Long productId;
    private final int quantity;

    public ProductQuantity(Long productId, int quantity) {
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
