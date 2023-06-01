package cart.entity;

public class OrderItemEntity {
    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final int quantity;
    private final int price;

    public OrderItemEntity(Long id, Long orderId, Long productId, int quantity, int price) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderItemEntity(Long orderId, Long productId, int quantity, int price) {
        this(null, orderId, productId, quantity, price);
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

    public int getPrice() {
        return price;
    }
}
