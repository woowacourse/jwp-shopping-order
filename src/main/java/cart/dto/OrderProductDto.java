package cart.dto;

public class OrderProductDto {
    private final Long productId;
    private final int quantity;

    public OrderProductDto(Long productId, int quantity) {
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
