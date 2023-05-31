package cart.application.dto.order;

import java.util.List;

public class OrderRequest {

    private final Long couponId;
    private final List<OrderProductRequest> items;

    public OrderRequest() {
        this(null, null);
    }

    public OrderRequest(final Long couponId, final List<OrderProductRequest> items) {
        this.couponId = couponId;
        this.items = items;
    }

    public Long getCouponId() {
        return couponId;
    }

    public List<OrderProductRequest> getItems() {
        return items;
    }
}
