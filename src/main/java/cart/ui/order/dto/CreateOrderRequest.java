package cart.ui.order.dto;

import java.util.List;

public class CreateOrderRequest {

    private List<CreateOrderItemRequest> orderItems;
    private CreateOrderDiscountRequest orderDiscounts;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(final List<CreateOrderItemRequest> orderItems, final CreateOrderDiscountRequest orderDiscounts) {
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
