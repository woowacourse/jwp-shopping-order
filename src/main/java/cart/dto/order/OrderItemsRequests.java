package cart.dto.order;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderItemsRequests {

    @Min(value = 0, message = "배송비는 음수일 수 없습니다.")
    private int deliveryFee;
    @Valid
    @NotNull
    private List<OrderItemRequest> orderItemRequests;

    public OrderItemsRequests(final List<OrderItemRequest> orderItemRequests) {
        this.orderItemRequests = orderItemRequests;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public List<OrderItemRequest> getOrderItemRequests() {
        return orderItemRequests;
    }
}
