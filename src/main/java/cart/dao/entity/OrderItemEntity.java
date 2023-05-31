package cart.dao.entity;

public class OrderItemEntity {

    private Long id;
    private final long orderId;
    private final long productId;
    private final int quantity;
    private final int priceAtOrderTime;

    public OrderItemEntity(long orderId, long productId, int quantity, int priceAtOrderTime) {
        this(null, orderId, productId, quantity, priceAtOrderTime);
    }

    public OrderItemEntity(Long id, long orderId, long productId, int quantity,
        int priceAtOrderTime) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.priceAtOrderTime = priceAtOrderTime;
    }

    public Long getId() {
        return id;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPriceAtOrderTime() {
        return priceAtOrderTime;
    }
}
