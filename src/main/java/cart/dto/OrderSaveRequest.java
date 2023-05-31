package cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "주문 추가 Request")
public class OrderSaveRequest {

    @Schema(description = "주문할 상품들", example = "[1, 2]")
    private final List<Long> orderItems;

    @Schema(description = "사용자 쿠폰 Id", example = "1")
    private final Long couponId;

    public OrderSaveRequest(final List<Long> orderItems, final Long couponId) {
        this.orderItems = orderItems;
        this.couponId = couponId;
    }

    public List<Long> getOrderItems() {
        return orderItems;
    }

    public Long getCouponId() {
        return couponId;
    }

}
