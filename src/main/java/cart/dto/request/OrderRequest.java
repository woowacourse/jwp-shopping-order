package cart.dto.request;

import java.util.List;

public class OrderRequest {
    private final List<Long> selectCartIds;
    private final Long couponId;

    public OrderRequest(List<Long> cartProductIds, Long couponId) {
        this.selectCartIds = cartProductIds;
        this.couponId = couponId;
    }

    public List<Long> getSelectCartIds() {
        return selectCartIds;
    }

    public Long getCouponId() {
        return couponId;
    }
}
