package cart.controller.request;

import java.util.List;
import java.util.Optional;

public class OrderRequestDto {

    private List<Long> cartItemIds;
    private Optional<Long> couponId;

    private OrderRequestDto() {
    }

    public OrderRequestDto(final List<Long> cartItemIds) {
        this(cartItemIds, Optional.empty());
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

    @Override
    public String toString() {
        return "OrderRequestDto{" +
                "cartItemIds=" + cartItemIds +
                ", couponId=" + couponId +
                '}';
    }

}
