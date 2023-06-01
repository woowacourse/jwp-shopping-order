package cart.dto;

import cart.domain.Order;
import cart.domain.OrderItem;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private long id;
    private BigDecimal price;
    private Timestamp orderDate;
    private List<OrderItemResponse> orders;

    public OrderResponse(
        long id,
        BigDecimal price,
        Timestamp orderDate,
        List<OrderItemResponse> orders) {
        this.id = id;
        this.price = price;
        this.orderDate = orderDate;
        this.orders = orders;
    }

    public static OrderResponse fromOrder(Order order) {
        return new OrderResponse(
            order.getId(),
            order.calculateTotalPrice().getValue(),
            order.getCreatedAt(),
            getOrderItemResponses(order.getItems())
        );
    }

    private static List<OrderItemResponse> getOrderItemResponses(List<OrderItem> orderItems) {
        return orderItems.stream()
            .map(OrderItemResponse::fromOrderItem)
            .collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public List<OrderItemResponse> getOrders() {
        return orders;
    }
}
