package cart.entity;

public class OrderItemEntity {
    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final Integer quantity;

    public OrderItemEntity(final Long id, final Long orderId, final Long productId, final Integer quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public OrderItemEntity(final Long orderId, final Long productId, final Integer quantity) {
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

    public Integer getQuantity() {
        return quantity;
    }
}
