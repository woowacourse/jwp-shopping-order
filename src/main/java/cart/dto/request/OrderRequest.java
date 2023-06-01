package cart.dto.request;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;

public class OrderRequest {

    @PositiveOrZero(message = "사용할 포인트는 음수일 수 없습니다.")
    private int usedPoints;

    @NotEmpty(message = "주문할 장바구니 상품 ID를 입력해주세요.")
    private List<Long> cartItemIds;

    private OrderRequest() {
    }

    public OrderRequest(int usedPoints, List<Long> cartItemIds) {
        this.usedPoints = usedPoints;
        this.cartItemIds = cartItemIds;
    }

    public int getUsedPoints() {
        return usedPoints;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }
}
