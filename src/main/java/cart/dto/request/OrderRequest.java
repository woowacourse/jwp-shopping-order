package cart.dto.request;

import java.util.List;

public class OrderRequest {
    private List<CartItemRequest> products;
    private Long couponId;

    public OrderRequest() {
    }

    public OrderRequest(List<CartItemRequest> products, Long couponId) {
        this.products = products;
        this.couponId = couponId;
    }

    public List<CartItemRequest> getProducts() {
        return products;
    }

    public Long getCouponId() {
        return couponId;
    }
}
