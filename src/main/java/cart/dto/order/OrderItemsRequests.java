package cart.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderItemsRequests {

    @Min(value = 0, message = "배송비는 음수일 수 없습니다.")
    private int deliveryFee;
    @Valid
    @NotNull
    @JsonProperty("orderItems")
    private List<OrderItemRequest> orderItemRequests;

    public OrderItemsRequests(final int deliveryFee, final List<OrderItemRequest> orderItemRequests) {
        this.deliveryFee = deliveryFee;
        this.orderItemRequests = orderItemRequests;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public List<OrderItemRequest> getOrderItemRequests() {
        return orderItemRequests;
    }
}
