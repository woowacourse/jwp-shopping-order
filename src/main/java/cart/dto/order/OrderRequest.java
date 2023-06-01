package cart.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderRequest {

    @JsonProperty("product")
    private OrderProductRequest productRequest;
    @Min(value = 0, message = "상품 개수는 음수일 수 없습니다.")
    private int quantity;
    @NotNull
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
