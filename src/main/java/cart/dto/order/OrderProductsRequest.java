package cart.dto.order;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

public class OrderProductsRequest {

    @NotNull
    @Size(min = 1, message = "개수는 최소 1개 이상이어야 합니다. value=${validatedValue}")
    private List<Long> orderItems;

    @PositiveOrZero(message = "입력 가능한 포인트는 0포인트 부터입니다.")
    private int usedPoint;

    public OrderProductsRequest() {
    }

    public OrderProductsRequest(List<Long> orderItems, int usedPoint) {
        this.orderItems = orderItems;
        this.usedPoint = usedPoint;
    }

    public List<Long> getOrderItems() {
        return orderItems;
    }

    public int getUsedPoint() {
        return usedPoint;
    }
}
