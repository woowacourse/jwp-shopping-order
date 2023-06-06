package cart.ui.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Schema(description = "주문 요청")
public class OrderRequest {

    @Schema(description = "장바구니 상품 ID", example = "[1, 3, 5, 7]")
    @NotNull(message = "장바구니 상품 ID 목록이 필요합니다.")
    @NotEmpty(message = "장바구니 상품 ID가 최소한 1개는 필요합니다.")
    private List<Long> cartItemIds;

    @Schema(description = "사용할 포인트", example = "1000")
    @NotNull(message = "사용할 포인트는 존재해야 합니다.")
    @PositiveOrZero(message = "사용할 포인트는 음수일 수 없습니다.")
    private Integer point;

    private OrderRequest() {
    }

    public OrderRequest(List<Long> cartItemIds, Integer point) {
        this.cartItemIds = cartItemIds;
        this.point = point;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public Integer getPoint() {
        return point;
    }
}
