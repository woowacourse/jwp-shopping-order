package cart.dto;

public class OrderInfo {
    private Long productId;
    private Long quantity;

    public OrderInfo() {
    }

    public OrderInfo(final Long productId, final Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
