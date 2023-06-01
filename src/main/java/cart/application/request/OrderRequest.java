package cart.application.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public class OrderRequest {

    @NotNull(message = "주문할 장바구니 목록이 null일 수 없습니다.")
    @NotEmpty(message = "주문할 장바구니 목록이 비어있을 수 없습니다.")
    private List<Long> cartItemIds;

    @PositiveOrZero(message = "사용할 포인트는 음수일 수 없습니다.")
    private int point;

    public OrderRequest() {
    }

    public OrderRequest(List<Long> cartItemIds, int point) {
        this.cartItemIds = cartItemIds;
        this.point = point;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public int getPoint() {
        return point;
    }
}
