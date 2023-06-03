package cart.dto.response;

import cart.domain.order.Order;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private final Long id;
    private final int price;
    private final Timestamp orderDate;
    private final List<OrderItemResponse> orders;

    public OrderResponse(
            final Long id,
            final int price,
            final Timestamp orderDate,
            final List<OrderItemResponse> orders
    ) {
        this.id = id;
        this.price = price;
        this.orderDate = orderDate;
        this.orders = orders;
    }

    public static OrderResponse from(final Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalPrice().price(),
                order.getOrderTime(),
                order.getOrderItems()
                        .stream()
                        .map(OrderItemResponse::from)
                        .collect(Collectors.toUnmodifiableList())
        );
    }

    public Long getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public List<OrderItemResponse> getOrders() {
        return orders;
    }
}
