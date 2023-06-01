package cart.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OrderRequest {

    @JsonProperty("product")
    private OrderProductRequest productRequest;
    private int quantity;
    @JsonProperty("couponId")
    private List<Long> memberCouponIds;

    private OrderRequest() {
    }

    public OrderRequest(final OrderProductRequest productRequest, final int quantity, final List<Long> memberCouponIds) {
        this.productRequest = productRequest;
        this.quantity = quantity;
        this.memberCouponIds = memberCouponIds;
    }

    public OrderProductRequest getProductRequest() {
        return productRequest;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<Long> getMemberCouponIds() {
        return memberCouponIds;
    }
}
