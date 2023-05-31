package cart.controller.request;

import java.util.List;

public class OrderRequestDto {

    private final List<Long> cartItemIds;
    private final Long couponId;

    private OrderRequestDto() {
        this(null);
    }

    public OrderRequestDto(final List<Long> cartItemIds) {
        this(cartItemIds, null);
    }

    public OrderRequestDto(final List<Long> cartItemIds, final Long couponId) {
        this.cartItemIds = cartItemIds;
        this.couponId = couponId;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public Long getCouponId() {
        return couponId;
    }

}
