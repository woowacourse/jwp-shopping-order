package cart.application.dto.order;

import java.util.List;

public class OrderCartItemsRequest {

    private Long couponId;
    private List<OrderCartItemProductRequest> products;

    public OrderCartItemsRequest() {
    }

    public OrderCartItemsRequest(final Long couponId,
            final List<OrderCartItemProductRequest> products) {
        this.couponId = couponId;
        this.products = products;
    }

    public Long getCouponId() {
        return couponId;
    }

    public List<OrderCartItemProductRequest> getProducts() {
        return products;
    }
}
