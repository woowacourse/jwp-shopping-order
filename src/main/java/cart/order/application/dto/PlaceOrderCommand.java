package cart.order.application.dto;

import java.util.List;

public class PlaceOrderCommand {

    private final Long memberId;
    private final List<Long> cartItemIds;
    private final List<Long> couponIds;

    public PlaceOrderCommand(Long memberId, List<Long> cartItemIds, List<Long> couponIds) {
        this.memberId = memberId;
        this.cartItemIds = cartItemIds;
        this.couponIds = couponIds;
    }

    public Long getMemberId() {
        return memberId;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public List<Long> getCouponIds() {
        return couponIds;
    }
}
