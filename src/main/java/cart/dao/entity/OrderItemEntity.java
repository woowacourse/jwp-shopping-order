package cart.dao.entity;

public class OrderItemEntity {

    private final Long id;
    private final long productId;
    private final long orderId;
    private final int quantity;

    public OrderItemEntity(long productId, long orderId, int quantity) {
        this(null, productId, orderId, quantity);
    }

    public OrderItemEntity(Long id, long productId, long orderId, int quantity) {
        this.id = id;
        this.productId = productId;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public long getProductId() {
        return productId;
    }

    public long getOrderId() {
        return orderId;
    }

    public int getQuantity() {
        return quantity;
    }
}
