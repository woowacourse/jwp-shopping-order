package cart.controller.request;

import java.util.List;
import java.util.Optional;

public class OrderRequestDto {

    private final List<Long> cartItemIds;
    private final Optional<Long> couponId;

    private OrderRequestDto() {
        this(null);
    }

    public OrderRequestDto(final List<Long> cartItemIds) {
        this(cartItemIds, null);
    }

    public OrderRequestDto(final List<Long> cartItemIds, final Optional<Long> couponId) {
        this.cartItemIds = cartItemIds;
        this.couponId = couponId;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public Optional<Long> getCouponId() {
        return couponId;
    }

}
