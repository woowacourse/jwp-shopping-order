package cart.entity;

import cart.domain.order.OrderItem;
import java.util.List;
import java.util.stream.Collectors;

public class OrderItemEntity {

    private final Long id;
    private final Long ordersId;
    private final Long productId;
    private final int quantity;

    public OrderItemEntity(final Long id, final Long ordersId, final Long productId, final int quantity) {
        this.id = id;
        this.ordersId = ordersId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static List<OrderItemEntity> from(Long orderId, List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> new OrderItemEntity(orderItem.getId(), orderId, orderItem.getProduct().getId(),
                        orderItem.getQuantity()))
                .collect(Collectors.toUnmodifiableList());
    }

    public Long getId() {
        return id;
    }

    public Long getOrdersId() {
        return ordersId;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
