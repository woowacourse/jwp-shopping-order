package cart.service.request;

import java.util.List;
import java.util.Optional;

public class OrderRequestDto {

    private final List<Long> cartItemIds;
    private final Long memberCouponId;

    private OrderRequestDto() {
        this(null, null);
    }

    public OrderRequestDto(final List<Long> cartItemIds, final Long memberCouponId) {
        this.cartItemIds = cartItemIds;
        this.memberCouponId = memberCouponId;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public Long getMemberCouponId() {
        return memberCouponId;
    }

    public Optional<Long> getOptionalCouponId() {
        return Optional.ofNullable(memberCouponId);
    }
}
