package cart.ui.order.dto.request;

import java.util.List;

public class CreateOrderRequest {

    private List<CreateOrderItemRequest> orderItems;
    private CreateOrderDiscountRequest orderDiscounts;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(List<CreateOrderItemRequest> orderItems, CreateOrderDiscountRequest orderDiscounts) {
        this.orderItems = orderItems;
        this.orderDiscounts = orderDiscounts;
    }

    public List<CreateOrderItemRequest> getOrderItems() {
        return orderItems;
    }

    public CreateOrderDiscountRequest getOrderDiscounts() {
        return orderDiscounts;
    }

}
