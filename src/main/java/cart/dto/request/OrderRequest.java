package cart.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public class OrderRequest {

    @NotNull(message = "장바구니 상품을 선택해야합니다.")
    private final List<Long> cartIds;

    @PositiveOrZero(message = "사용하는 포인트는 음수가 될 수 없습니다. 입력값: ${validatedValue}")
    private final Integer point;

    public OrderRequest(List<Long> cartIds, Integer point) {
        this.cartIds = cartIds;
        this.point = point;
    }

    public List<Long> getCartIds() {
        return cartIds;
    }

    public Integer getPoint() {
        return point;
    }
}
