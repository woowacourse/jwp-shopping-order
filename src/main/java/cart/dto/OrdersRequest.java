package cart.dto;

import java.util.List;

public class OrdersRequest {
    private List<Long> cartProductIds;
    private Long couponId;

    private OrdersRequest() {

    }

    public OrdersRequest(List<Long> cartProductIds,  Long couponId) {
        this.cartProductIds = cartProductIds;
        this.couponId = couponId;
    }

    public List<Long> getCartProductIds() {
        return cartProductIds;
    }

    public Long getCouponId() {
        return couponId;
    }
}
