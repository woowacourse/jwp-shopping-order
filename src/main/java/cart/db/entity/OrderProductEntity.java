package cart.db.entity;

public class OrderProductEntity {

    private Long id;
    private final Long orderId;
    private final Long productId;
    private final int orderedProductPrice;
    private final int quantity;

    public OrderProductEntity(
            final Long orderId,
            final Long productId,
            final int orderedProductPrice,
            final int quantity
    ) {
        this.orderId = orderId;
        this.productId = productId;
        this.orderedProductPrice = orderedProductPrice;
        this.quantity = quantity;
    }

    public OrderProductEntity(
            final Long id,
            final Long orderId,
            final Long productId,
            final int orderedProductPrice,
            final int quantity
    ) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.orderedProductPrice = orderedProductPrice;
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

    public int getOrderedProductPrice() {
        return orderedProductPrice;
    }

    public int getQuantity() {
        return quantity;
    }
}
