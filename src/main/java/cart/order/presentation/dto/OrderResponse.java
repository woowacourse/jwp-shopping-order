package cart.order.presentation.dto;

import cart.order.domain.Order;
import cart.order.domain.OrderItem;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private final Long orderId;
    private final List<OrderItemResponse> orderItems;
    private final int totalPrice;

    public OrderResponse(Long orderId, List<OrderItemResponse> orderItems, int totalPrice) {
        this.orderId = orderId;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
    }

    public static OrderResponse from(Order order, int totalPrice) {
        return new OrderResponse(
                order.getId(),
                OrderItemResponse.from(order.getOrderItems()),
                totalPrice);
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public static class OrderItemResponse {
        private final Long orderItemId;
        private final String name;
        private final int price;
        private final int orderedPrice;
        private final int quantity;
        private final String imageUrl;

        public OrderItemResponse(Long orderItemId, String name, int price, int orderedPrice, int quantity, String imageUrl) {
            this.orderItemId = orderItemId;
            this.name = name;
            this.price = price;
            this.orderedPrice = orderedPrice;
            this.quantity = quantity;
            this.imageUrl = imageUrl;
        }

        private static OrderItemResponse from(OrderItem orderItem) {
            return new OrderItemResponse(
                    orderItem.getId(),
                    orderItem.getName(),
                    orderItem.price(),
                    orderItem.orderedPrice(),
                    orderItem.getQuantity(),
                    orderItem.getImageUrl());
        }

        public static List<OrderItemResponse> from(List<OrderItem> orderItems) {
            return orderItems.stream()
                    .map(OrderItemResponse::from)
                    .collect(Collectors.toList());
        }

        public Long getOrderItemId() {
            return orderItemId;
        }

        public String getName() {
            return name;
        }

        public int getPrice() {
            return price;
        }

        public int getOrderedPrice() {
            return orderedPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
}
