package cart.dao.entity;

public class OrderDetailEntity {
    private Long id;
    private Long orderId;
    private Long productId;
    private Long quantity;

    public OrderDetailEntity(final Long id, final Long orderId, final Long productId, final Long quantity) {
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

    public Long getQuantity() {
        return quantity;
    }
}
