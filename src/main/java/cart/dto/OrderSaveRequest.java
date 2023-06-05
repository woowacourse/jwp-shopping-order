package cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(description = "주문 추가 Request")
public class OrderSaveRequest {

    @Schema(description = "주문할 상품들", example = "[1, 2]")
    @Valid
    @NotNull
    @Size(min = 1, message = "주문할 상품 개수는 최소 1개 이상입니다.")
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
