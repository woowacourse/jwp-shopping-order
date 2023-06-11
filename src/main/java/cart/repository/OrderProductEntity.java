package cart.repository;

public class OrderProductEntity {

    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final Integer quantity;

    public OrderProductEntity(Long id, Long orderId, Long productId, Integer quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public OrderProductEntity(Long orderId, Long productId, Integer quantity) {
        this(null, orderId, productId, quantity);
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

    public Integer getQuantity() {
        return quantity;
    }
}
