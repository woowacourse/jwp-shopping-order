package cart.controller.request;

import java.util.List;
import java.util.Optional;
import javax.validation.constraints.Size;

public class OrderRequestDto {

    @Size(min = 1)
    private List<Long> cartItemIds;
    private Long couponId;

    private OrderRequestDto() {
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

    public Optional<Long> getCouponId() {
        return Optional.of(couponId);
    }

    @Override
    public String toString() {
        return "OrderRequestDto{" +
                "cartItemIds=" + cartItemIds +
                ", couponId=" + couponId +
                '}';
    }

}
