package cart.dao.dto;

public class OrderProductDto {

    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final Long quantity;

    public OrderProductDto(final Long id, final Long orderId, final Long productId, final Long quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public OrderProductDto(final Long orderId, final Long productId, final Long quantity) {
        this(null, orderId, productId, quantity);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

}
