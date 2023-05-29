package cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "주문 Request")
public class OrderRequest {
    @Schema(description = "주문 상품")
    private final List<OrderItemIdDto> orderItems;
    @Schema(description = "쿠폰 Id", example = "1")
    private final Long couponId;

    public OrderRequest(final List<OrderItemIdDto> orderItems, final Long couponId) {
        this.orderItems = orderItems;
        this.couponId = couponId;
    }

    public List<OrderItemIdDto> getOrderItems() {
        return orderItems;
    }

    public Long getCouponId() {
        return couponId;
    }
}
