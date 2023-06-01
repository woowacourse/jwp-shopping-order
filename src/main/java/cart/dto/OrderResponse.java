package cart.dto;

import cart.domain.CartItem;

import java.util.List;

public class OrderResponse {

    private final OrderItemDto order;
    private final int totalPrice;

    private OrderResponse(Long orderId, List<CartItem> cartItems, int price) {
        this.order = OrderItemDto.of(orderId, cartItems);
        this.totalPrice = price;
    }

    public static OrderResponse of(OrderDto orderDto) {
        return new OrderResponse(orderDto.getId(), orderDto.getCartItems(), orderDto.getPrice());
    }

    public OrderItemDto getOrder() {
        return order;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

}
