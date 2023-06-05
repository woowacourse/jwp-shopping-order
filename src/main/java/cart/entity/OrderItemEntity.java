package cart.entity;

public class OrderItemEntity {

    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final Long priceAtOrder;
    private final int quantity;

    public OrderItemEntity(
            final Long id,
            final Long orderId,
            final Long productId,
            final Long priceAtOrder,
            final int quantity
    ) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.priceAtOrder = priceAtOrder;
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

    public Long getPriceAtOrder() {
        return priceAtOrder;
    }

    public int getQuantity() {
        return quantity;
    }
}
