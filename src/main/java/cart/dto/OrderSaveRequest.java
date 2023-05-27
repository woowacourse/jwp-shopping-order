package cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "주문 Request")
public class OrderSaveRequest {

    @Schema(description = "장바구니 상품 아이디 목록")
    private final List<ItemIdDto> orderItems;

    @Schema(description = "쿠폰 아이디", example = "1")
    private final Long couponId;

    public OrderSaveRequest(final List<ItemIdDto> orderItems, final Long couponId) {
        this.orderItems = orderItems;
        this.couponId = couponId;
    }

    public List<ItemIdDto> getOrderItems() {
        return orderItems;
    }

    public Long getCouponId() {
        return couponId;
    }
}
