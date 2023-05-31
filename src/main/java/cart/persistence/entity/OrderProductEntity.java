package cart.persistence.entity;

public class OrderProductEntity {

    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final Integer orderProductPrice;
    private final Integer orderQuantity;

    public OrderProductEntity(final Long orderId, final Long productId,
                              final Integer orderProductPrice, final Integer orderQuantity) {
        this(null, orderId, productId, orderProductPrice, orderQuantity);
    }

    public OrderProductEntity(final Long id, final Long orderId, final Long productId,
                              final Integer orderProductPrice, final Integer orderQuantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.orderProductPrice = orderProductPrice;
        this.orderQuantity = orderQuantity;
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

    public Integer getOrderProductPrice() {
        return orderProductPrice;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }
}
