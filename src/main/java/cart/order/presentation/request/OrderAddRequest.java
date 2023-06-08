package cart.order.presentation.request;

import cart.order.application.dto.CartItemDto;
import cart.order.application.dto.OrderAddDto;
import java.util.List;

public class OrderAddRequest {
    private final List<CartItemDto> cartItems;
    private final Integer totalProductPrice;
    private final Integer totalDeliveryFee;
    private final Integer usePoint;
    private final Integer totalPrice;

    public OrderAddRequest(List<CartItemDto> cartItems, Integer totalProductPrice, Integer totalDeliveryFee,
                           Integer usePoint, Integer totalPrice) {
        this.cartItems = cartItems;
        this.totalProductPrice = totalProductPrice;
        this.totalDeliveryFee = totalDeliveryFee;
        this.usePoint = usePoint;
        this.totalPrice = totalPrice;
    }

    public OrderAddDto toDto() {
        return new OrderAddDto(cartItems, totalProductPrice, totalDeliveryFee, usePoint, totalPrice);
    }

    public List<CartItemDto> getCartItems() {
        return cartItems;
    }

    public Integer getTotalProductPrice() {
        return totalProductPrice;
    }

    public Integer getTotalDeliveryFee() {
        return totalDeliveryFee;
    }

    public Integer getUsePoint() {
        return usePoint;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }
}
