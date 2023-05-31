package cart.dto;

public class OrderInfo {

    private Long productId;
    private Integer quantity;

    public OrderInfo() {
    }

    public OrderInfo(final Long productId, final Integer quantity) {
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
