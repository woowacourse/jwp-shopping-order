package cart.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public class OrderRequest {
    @NotEmpty
    private List<CartItemRequest> products;
    @NotNull
    @Positive
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
