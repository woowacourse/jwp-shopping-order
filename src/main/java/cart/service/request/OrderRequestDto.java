package cart.service.request;

import java.util.List;
import java.util.Optional;

public class OrderRequestDto {

    private final List<Long> cartItemIds;
    private final Long couponId;

    private OrderRequestDto() {
        this(null, null);
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

    public Optional<Long> getOptionalCouponId() {
        return Optional.ofNullable(couponId);
    }
}
