package cart.dto.order;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

public class OrderProductsRequest {

    @NotNull
    @Size(min = 1, message = "개수는 최소 1개 이상이어야 합니다. value=${validatedValue}")
    private List<Long> cartItemIds;

    @PositiveOrZero(message = "입력 가능한 포인트는 0포인트 부터입니다.")
    private int usedPoint;

    public OrderProductsRequest() {
    }

    public OrderProductsRequest(List<Long> cartItemIds, int usedPoint) {
        this.cartItemIds = cartItemIds;
        this.usedPoint = usedPoint;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public int getUsedPoint() {
        return usedPoint;
    }
}
