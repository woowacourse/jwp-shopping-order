package cart.entity;

public class OrderItemEntity {
    private final Long id;
    private final Long productId;
    private final Long orderId;
    private final int quantity;

    public OrderItemEntity(final Long id, final Long productId, final Long orderId, final int quantity) {
        this.id = id;
        this.productId = productId;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    public static OrderItemEntity toCreate(Long productId, Long orderId, int quantity) {
        return new OrderItemEntity(null, productId, orderId, quantity);
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public int getQuantity() {
        return quantity;
    }
}
