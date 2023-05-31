package cart.entity;

public class OrderItemEntity {

    private final Long id;
    private final Long order_id;
    private final Long productId;
    private final int priceAtOrder;
    private final int quantity;

    public OrderItemEntity(
            final Long id,
            final Long order_id,
            final Long productId,
            final int priceAtOrder,
            final int quantity
    ) {
        this.id = id;
        this.order_id = order_id;
        this.productId = productId;
        this.priceAtOrder = priceAtOrder;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getOrder_id() {
        return order_id;
    }

    public Long getProductId() {
        return productId;
    }

    public int getPriceAtOrder() {
        return priceAtOrder;
    }

    public int getQuantity() {
        return quantity;
    }
}
