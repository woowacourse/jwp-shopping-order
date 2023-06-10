package cart.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public class OrderRequest {

    @PositiveOrZero(message = "사용하는 포인트는 음수일 수 없습니다.")
    private int usedPoints;
    @NotEmpty(message = "최소 하나 이상의 상품이 포함되어야 합니다.")
    private List<Long> cartItemIds;

    public OrderRequest() {

    }

    public OrderRequest(final int usedPoints, final List<Long> cartItemIds) {
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
