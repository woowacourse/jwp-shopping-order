package cart.entity;

public class OrderItemEntity {

    private final Long id;
    private final long orderId;
    private final long productId;
    private final int quantity;

    public OrderItemEntity(final Long id, final long orderId, final long productId, final int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public OrderItemEntity(final long orderId, final long productId, final int quantity) {
        this(null, orderId, productId, quantity);
    }

    public static OrderItemEntity of(final long id, final OrderItemEntity orderItemEntity) {
        return new OrderItemEntity(id, orderItemEntity.orderId, orderItemEntity.productId, orderItemEntity.quantity);
    }

    public Long getId() {
        return id;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
