package cart.dto;

import java.util.List;

public class OrderRequest {

    private List<OrderItemDto> orderItems;
    private PaymentDto payment;

    public OrderRequest() {
    }

    public OrderRequest(final List<OrderItemDto> orderItems, final PaymentDto payment) {
        this.orderItems = orderItems;
        this.payment = payment;
    }

    public List<OrderItemDto> getOrderItems() {
        return orderItems;
    }

    public PaymentDto getPayment() {
        return payment;
    }
}
