package cart.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Schema(description = "주문 Request")
public class OrderSaveRequest {

    @Schema(description = "장바구니 상품 아이디 목록")
    @NotEmpty(message = "장바구니는 비어있을 수 없습니다.")
    private final List<@Positive(message = "주문 상품의 아이디는 1 이상의 값이어야 합니다.") Long> orderItemIds;

    @Schema(description = "쿠폰 아이디", example = "1")
    @Positive(message = "쿠폰 아이디는 1 이상의 값이어야 합니다.")
    private final Long couponId;

    public OrderSaveRequest(final List<Long> orderItemIds, final Long couponId) {
        this.orderItemIds = orderItemIds;
        this.couponId = couponId;
    }

    public List<Long> getOrderItemIds() {
        return orderItemIds;
    }

    public Long getCouponId() {
        return couponId;
    }
}
