package cart.controller.request;

import java.util.List;

public class OrderRequestDto {

    private final List<Long> cartItemIds;

    private OrderRequestDto() {
        this(null);
    }

    public OrderRequestDto(final List<Long> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }
}
