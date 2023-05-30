package cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "주문 Request")
public class OrderRequest {
    @Schema(description = "주문 상품")
    private final List<Long> orderItemIds;
    @Schema(description = "쿠폰 Id", example = "1")
    private final Long couponId;

    public OrderRequest(final List<Long> orderItemIds, final Long couponId) {
        this.orderItemIds = orderItemIds;
        this.couponId = couponId;
    }

    public OrderRequest(final List<Long> orderItemIds) {
        this(orderItemIds, null);
    }

    public List<Long> getOrderItemIds() {
        return orderItemIds;
    }

    public Long getCouponId() {
        return couponId;
    }
}
