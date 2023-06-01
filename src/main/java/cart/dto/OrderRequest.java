package cart.dto;

import java.util.List;

public class OrderRequest {

    private List<ItemRequest> items;
    private Long couponId;

    public OrderRequest() {
    }

    public OrderRequest(final List<ItemRequest> items) {
        this.items = items;
    }

    public OrderRequest(final List<ItemRequest> items, final Long couponId) {
        this.items = items;
        this.couponId = couponId;
    }

    public List<ItemRequest> getItems() {
        return items;
    }

    public Long getCouponId() {
        return couponId;
    }
}
