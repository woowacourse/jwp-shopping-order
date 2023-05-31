package cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OrderRequest {

    @JsonProperty("product")
    private final OrderProductRequest productRequest;
    private final int quantity;
    private final List<Long> couponId;

    public OrderRequest(final OrderProductRequest productRequest, final int quantity, final List<Long> couponId) {
        this.productRequest = productRequest;
        this.quantity = quantity;
        this.couponId = couponId;
    }

    public OrderProductRequest getProductRequest() {
        return productRequest;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<Long> getCouponId() {
        return couponId;
    }
}
