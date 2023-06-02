package shop.persistence.entity;

public class OrderProductEntity {
    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final Integer orderedProductPrice;
    private final Integer quantity;

    public OrderProductEntity(Long id, Long orderId, Long productId, Integer orderedProductPrice, Integer quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.orderedProductPrice = orderedProductPrice;
        this.quantity = quantity;
    }

    public OrderProductEntity(Long orderId, Long productId, Integer orderedProductPrice, Integer quantity) {
        this(null, orderId, productId, orderedProductPrice, quantity);
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

    public Integer getOrderedProductPrice() {
        return orderedProductPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
