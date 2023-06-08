package cart.dto.request;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class OrderCreateRequest {
    @Valid
    private List<OrderItemCreateRequest> orderItems;

    @NotNull(message = "사용할 포인트는 반드시 포함되어야 합니다.")
    @PositiveOrZero(message = "사용할 포인트는 음수가 될 수 없습니다.")
    private Long spendPoint;

    private OrderCreateRequest() {
    }

    public OrderCreateRequest(List<OrderItemCreateRequest> orderItems, Long spendPoint) {
        this.orderItems = orderItems;
        this.spendPoint = spendPoint;
    }

    public List<OrderItemCreateRequest> getOrderItems() {
        return orderItems;
    }

    public Long getSpendPoint() {
        return spendPoint;
    }
}
