package cart.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OrderRequest {

    @JsonProperty("product")
    private OrderProductRequest productRequest;
    private int quantity;
    private List<Long> couponId;

    private OrderRequest() {
    }

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
