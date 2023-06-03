package cart.application.dto.order;

import java.util.List;

public class CreateOrderByCartItemsRequest {

    private Long couponId;
    private List<OrderProductRequest> products;

    public CreateOrderByCartItemsRequest() {
    }

    public CreateOrderByCartItemsRequest(final Long couponId,
            final List<OrderProductRequest> products) {
        this.couponId = couponId;
        this.products = products;
    }

    public Long getCouponId() {
        return couponId;
    }

    public List<OrderProductRequest> getProducts() {
        return products;
    }
}
