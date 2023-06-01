package cart.application.dto.order;

import java.util.List;

public class OrderCartItemsRequest {

    private List<Long> cartItemIds;
    private Long couponId;

    public OrderCartItemsRequest() {
    }

    public OrderCartItemsRequest(final List<Long> cartItemIds, final Long couponId) {
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
