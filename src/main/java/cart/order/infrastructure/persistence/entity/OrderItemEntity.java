package cart.order.infrastructure.persistence.entity;

public class OrderItemEntity {

    private final Long id;
    private final int quantity;
    private final Long productId;
    private final Long orderId;

    public OrderItemEntity(Long id, int quantity, Long productId, Long orderId) {
        this.id = id;
        this.quantity = quantity;
        this.productId = productId;
        this.orderId = orderId;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOrderId() {
        return orderId;
    }
}
