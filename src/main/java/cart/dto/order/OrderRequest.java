package cart.dto.order;

import java.util.List;

public class OrderRequest {
    private final List<Long> selectCartIds;
    private final Long couponId;

    public OrderRequest(List<Long> selectCartIds, Long couponId) {
        this.selectCartIds = selectCartIds;
        this.couponId = couponId;
    }

    public List<Long> getSelectCartIds() {
        return selectCartIds;
    }

    public Long getCouponId() {
        return couponId;
    }
}
