package cart.dto.request;

import java.util.List;

public class OrderRequest {
    private final List<Long> selectedCartIds;
    private final Long couponId;

    public OrderRequest(List<Long> cartProductIds, Long couponId) {
        this.selectedCartIds = cartProductIds;
        this.couponId = couponId;
    }

    public List<Long> getSelectedCartIds() {
        return selectedCartIds;
    }

    public Long getCouponId() {
        return couponId;
    }
}
