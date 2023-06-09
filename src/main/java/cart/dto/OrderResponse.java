package cart.dto;

import cart.domain.Order;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final Long orderId;
    private final List<OrderItemResponse> products;
    private final Long totalPayments;
    private final String createdAt;
    private final String orderStatus;

    private OrderResponse(final Long orderId,
                          final List<OrderItemResponse> products,
                          final Long totalPayments,
                          final String createdAt,
                          final String orderStatus) {
        this.orderId = orderId;
        this.products = products;
        this.totalPayments = totalPayments;
        this.createdAt = createdAt;
        this.orderStatus = orderStatus;
    }

    public static List<OrderResponse> from(final List<Order> orders) {
        return orders.stream()
                .map(order -> new OrderResponse(
                        order.getId(),
                        OrderItemResponse.from(order.getOrderItems()),
                        order.totalPayments().getValue(),
                        DATE_FORMAT.format(order.getCreatedAt()),
                        order.getStatus().getValue()))
                .collect(Collectors.toList());
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderItemResponse> getProducts() {
        return products;
    }

    public Long getTotalPayments() {
        return totalPayments;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
