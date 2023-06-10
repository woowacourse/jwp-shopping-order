package cart.entity;

import cart.domain.OrderItem;

public class ProductOrderEntity {

    private final Long id;
    private final Long productId;
    private final Long orderId;
    private final int quantity;

    private ProductOrderEntity(final Long id, final Long productId, final Long orderId, final int quantity) {
        this.id = id;
        this.productId = productId;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    public static ProductOrderEntity of(final OrderItem orderItem, final Long orderId) {
        return new ProductOrderEntity(null, orderItem.getProduct().getId(), orderId, orderItem.getQuantity());
    }

    public static ProductOrderEntity of(final Long id, final Long productId, final Long orderId, final int quantity) {
        return new ProductOrderEntity(id, productId, orderId, quantity);
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
