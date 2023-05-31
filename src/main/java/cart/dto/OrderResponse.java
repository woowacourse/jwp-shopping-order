package cart.dto;

import cart.domain.Order;
import cart.domain.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long orderId;
    private List<OrderedProduct> orderedProducts;

    public OrderResponse() {
    }

    public OrderResponse(final Long orderId, final List<OrderedProduct> orderedProducts) {
        this.orderId = orderId;
        this.orderedProducts = orderedProducts;
    }

    public static OrderResponse from(final Order order) {
        final List<OrderItem> orderItems = order.getOrderItems();
        final List<OrderedProduct> orderedProducts = orderItems.stream()
                .map(OrderedProduct::from)
                .collect(Collectors.toUnmodifiableList());

        return new OrderResponse(order.getId(), orderedProducts);
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderedProduct> getOrderedProducts() {
        return orderedProducts;
    }
}
