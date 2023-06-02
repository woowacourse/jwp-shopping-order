package cart.controller.dto.response;

import cart.domain.Order;
import cart.domain.OrderItems;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private final int priceBeforeDiscount;
    private final int priceAfterDiscount;
    private final LocalDateTime date;
    private final List<OrderItemResponse> orderItems;

    private OrderResponse(final int priceBeforeDiscount, final int priceAfterDiscount,
                          final LocalDateTime date, final List<OrderItemResponse> orderItems) {
        this.priceBeforeDiscount = priceBeforeDiscount;
        this.priceAfterDiscount = priceAfterDiscount;
        this.date = date;
        this.orderItems = orderItems;
    }

    public static OrderResponse from(final Order order) {
        return new OrderResponse(
                order.getOriginalPrice(), order.getPaymentAmount(),
                order.getCreatedAt(), createOrderItemResponse(order.getOrderItems())
        );
    }

    private static List<OrderItemResponse> createOrderItemResponse(final OrderItems orderItems) {
        return orderItems.getOrderItems().stream()
                .map(OrderItemResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    public int getPriceBeforeDiscount() {
        return priceBeforeDiscount;
    }

    public int getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }
}
