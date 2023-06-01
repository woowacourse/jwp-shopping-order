package cart.entity;

public class OrderProductEntity {
    private final long id;
    private final long orderId;
    private final long productId;
    private final int quantity;

    public OrderProductEntity(final long id, final long orderId, final long productId, final int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getId() {
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
