package cart.dto.order;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderItemsRequests {

    @Min(value = 0, message = "배송비는 음수일 수 없습니다.")
    private final int deliveryFee;
    @Valid
    @NotNull
    private final List<OrderItemRequest> orderItems;

    public OrderItemsRequests(final int deliveryFee, final List<OrderItemRequest> orderItems) {
        this.deliveryFee = deliveryFee;
        this.orderItems = orderItems;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public List<OrderItemRequest> getOrderItems() {
        return orderItems;
    }
}
