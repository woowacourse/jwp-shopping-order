package cart.dto;

import cart.domain.Order;
import cart.domain.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private final Long id;
    private final long totalItemPrice;
    private final long discountPrice;
    private final long deliveryFee;
    private final List<OrderItemDto> orderItems;

    public OrderResponse(final Long id, final long totalItemPrice, final long discountPrice, final long deliveryFee, final List<OrderItemDto> orderItems) {
        this.id = id;
        this.totalItemPrice = totalItemPrice;
        this.discountPrice = discountPrice;
        this.deliveryFee = deliveryFee;
        this.orderItems = orderItems;
    }

    public static OrderResponse from(final Order order) {
        return new OrderResponse(
                order.getId(),
                order.calculateOrderPrice(),
                order.calculateDiscountPrice().getTotalItemsPrice(),
                order.getDeliveryFee(),
                mapToOrderItemDtos(order.getOrderItems().getOrderItems())
        );
    }

    private static List<OrderItemDto> mapToOrderItemDtos(final List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItemDto::from)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public long getTotalItemPrice() {
        return totalItemPrice;
    }

    public long getDiscountPrice() {
        return discountPrice;
    }

    public long getDeliveryFee() {
        return deliveryFee;
    }

    public List<OrderItemDto> getOrderItems() {
        return orderItems;
    }
}
