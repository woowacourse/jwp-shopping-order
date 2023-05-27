package cart.dto;

import java.util.List;
import java.util.stream.Collectors;

import cart.domain.Order;

public class OrderResponse {
    private final Long id;
    private final Integer totalPrice;
    private final List<CartItemResponse> cartItems;

    private OrderResponse(Long id, Integer totalPrice, List<CartItemResponse> cartItems) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.cartItems = cartItems;
    }

    public static OrderResponse of(Order order) {
        final List<CartItemResponse> cartItemResponses = order.getProducts()
                .stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toUnmodifiableList());
        return new OrderResponse(order.getId(), order.getPrice(), cartItemResponses);
    }

    public Long getId() {
        return id;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public List<CartItemResponse> getCartItems() {
        return cartItems;
    }
}
