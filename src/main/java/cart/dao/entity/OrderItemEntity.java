package cart.dao.entity;

public class OrderItemEntity {

    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final int quantity;

    public OrderItemEntity(Long orderId, Long productId, int quantity) {
        this(null, orderId, productId, quantity);
    }

    public OrderItemEntity(Long id, Long orderId, Long productId, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
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

    public int getQuantity() {
        return quantity;
    }
}
