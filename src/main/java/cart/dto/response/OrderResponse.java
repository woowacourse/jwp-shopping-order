package cart.dto.response;

import cart.domain.CartItem;
import cart.domain.Order;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private final long orderId;
    private final int totalPrice;
    private final List<CartItemResponse> cartItemResponses;

    public OrderResponse(final long orderId, final int totalPrice, final List<CartItemResponse> cartItemResponses) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.cartItemResponses = cartItemResponses;
    }

    public static OrderResponse of(final Order order) {
        final List<CartItemResponse> cartItemResponses = order.getCartItems().stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toUnmodifiableList());
        return new OrderResponse(order.getId(), order.getPrice(), cartItemResponses);
    }

    public long getOrderId() {
        return orderId;
    }

    public List<CartItemResponse> getCartItemResponse() {
        return cartItemResponses;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
