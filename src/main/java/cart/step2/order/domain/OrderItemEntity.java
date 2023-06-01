package cart.step2.order.domain;

public class OrderItemEntity {

    private final Long id;
    private final Long productId;
    private final Long orderId;
    private final int quantity;

    private OrderItemEntity(final Long id, final Long productId, final Long orderId, final int quantity) {
        this.id = id;
        this.productId = productId;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    public static OrderItemEntity of(final Long id, final Long productId, final Long orderId, final int quantity) {
        return new OrderItemEntity(id, productId, orderId, quantity);
    }

    public static OrderItemEntity createNonePkOrderItemEntity(final Long productId, final Long orderId, final int quantity) {
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
