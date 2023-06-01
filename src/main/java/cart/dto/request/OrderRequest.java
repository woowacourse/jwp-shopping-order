package cart.dto.request;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class OrderRequest {

    @Valid
    private List<OrderItemRequest> orderItems;

    @NotNull(message = "사용할 포인트는 반드시 포함되어야 합니다.")
    @PositiveOrZero(message = "사용할 포인트는 음수가 될 수 없습니다.")
    private Long spendPoint;

    private OrderRequest() {
    }

    public OrderRequest(List<OrderItemRequest> orderItems, Long spendPoint) {
        this.orderItems = orderItems;
        this.spendPoint = spendPoint;
    }

    public List<OrderItemRequest> getOrderItems() {
        return orderItems;
    }

    public Long getSpendPoint() {
        return spendPoint;
    }
}
