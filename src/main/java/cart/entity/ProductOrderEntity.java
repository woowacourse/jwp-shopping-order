package cart.entity;

public class ProductOrderEntity {

    private final Long id;
    private final Long productId;
    private final Long orderId;

    private ProductOrderEntity(final Long id, final Long productId, final Long orderId) {
        this.id = id;
        this.productId = productId;
        this.orderId = orderId;
    }

    public static ProductOrderEntity of(final Long productId, final Long orderId) {
        return new ProductOrderEntity(null, productId, orderId);
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
}
