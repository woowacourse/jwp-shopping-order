package cart.entity;

import cart.domain.product.Product;

public class OrderItemEntity {

    private final Long id;
    private final Long orderId;
    private final Product productEntity;
    private final int quantity;

    public OrderItemEntity(final Long id, final Long orderId, final Product productEntity, final int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productEntity = productEntity;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Product getProduct() {
        return productEntity;
    }

    public int getQuantity() {
        return quantity;
    }
}
