package cart.dto;

import cart.domain.CartItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private final inner order;
    private final int totalPrice;

    public OrderResponse(Long orderId, List<CartItem> cartItems, int price) {
        this.order = new inner(orderId, cartItems);
        this.totalPrice = price;
    }

    public inner getOrder() {
        return order;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public class inner {
        private final Long OrderId;
        private final List<CartItemResponse> orderItems;

        public inner(Long orderId, List<CartItem> orderItems) {
            this.OrderId = orderId;
            this.orderItems = orderItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
        }

        public Long getOrderId() {
            return OrderId;
        }

        public List<CartItemResponse> getOrderItems() {
            return orderItems;
        }
    }
}
