package cart.dto.response;

import cart.domain.order.Order;
import cart.dto.CartItemDto;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long id;
    private Integer originalPrice;
    private Integer actualPrice;
    private Integer deliveryFee;
    private List<CartItemDto> cartItems;

    private OrderResponse(
            Long id,
            Integer originalPrice,
            Integer actualPrice,
            Integer deliveryFee,
            List<CartItemDto> cartItems
    ) {
        this.id = id;
        this.originalPrice = originalPrice;
        this.actualPrice = actualPrice;
        this.deliveryFee = deliveryFee;
        this.cartItems = cartItems;
    }

    public static OrderResponse of(final Order order) {
        List<CartItemDto> cartItems = order.getOrderItems().stream()
                .map(CartItemDto::of)
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getOriginalTotalItemPrice().getValue(),
                order.getActualTotalItemPrice().getValue(),
                order.getDeliveryFee().getValue(),
                cartItems
        );
    }

    public Long getId() {
        return id;
    }

    public Integer getOriginalPrice() {
        return originalPrice;
    }

    public Integer getActualPrice() {
        return actualPrice;
    }

    public Integer getDeliveryFee() {
        return deliveryFee;
    }

    public List<CartItemDto> getCartItems() {
        return cartItems;
    }
}
