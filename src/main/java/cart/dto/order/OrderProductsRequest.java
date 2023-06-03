package cart.dto.order;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

public class OrderProductsRequest {

    @NotNull
    @Size(min = 1, message = "개수는 최소 1개 이상이어야 합니다. value=${validatedValue}")
    private List<Long> cartIds;

    @PositiveOrZero(message = "입력 가능한 포인트는 0포인트 부터입니다.")
    private int point;

    public OrderProductsRequest() {
    }

    public OrderProductsRequest(List<Long> cartItemIds, int usedPoint) {
        this.cartIds = cartItemIds;
        this.point = usedPoint;
    }

    public List<Long> getCartIds() {
        return cartIds;
    }

    public int getUsedPoint() {
        return point;
    }

    @Override
    public String toString() {
        return "OrderProductsRequest{" +
                "cartItemIds=" + cartIds +
                ", usedPoint=" + point +
                '}';
    }
}
