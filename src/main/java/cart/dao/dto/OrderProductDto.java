package cart.dao.dto;

import cart.domain.order.OrderProduct;

public class OrderProductDto {

    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final Integer quantity;

    public OrderProductDto(final Long id, final Long orderId, final Long productId, final Integer quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public OrderProductDto(final Long orderId, final Long productId, final Integer quantity) {
        this(null, orderId, productId, quantity);
    }

    public static OrderProductDto of(Long orderId, OrderProduct orderProduct) {
        return new OrderProductDto(orderProduct.getId(), orderId, orderProduct.getProduct().getId(),
                orderProduct.getQuantity().getValue());
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
