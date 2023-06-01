package cart.step2.order.domain;

public class OrderItem {

    private final Long id;
    private final Long productId;
    private final Long orderId;
    private final int quantity;

    private OrderItem(final Long id, final Long productId, final Long orderId, final int quantity) {
        this.id = id;
        this.productId = productId;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    public static OrderItem createNonePkOrder(final Long productId, final Long orderId, final int quantity) {
        return new OrderItem(null, productId, orderId, quantity);
    }

    public static OrderItem of(final Long id, final Long productId, final Long orderId, final int quantity) {
        return new OrderItem(id, productId, orderId, quantity);
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
